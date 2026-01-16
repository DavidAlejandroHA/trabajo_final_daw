package com.canarycode.appointments.security;

import com.canarycode.appointments.model.Token;
import com.canarycode.appointments.model.User;
import com.canarycode.appointments.repository.TokenRepository;
import com.canarycode.appointments.repository.UserRepository;
import com.canarycode.appointments.servicios.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter { // Cada vez que se haga una petición se ejecutará el filtro

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        if (request.getServletPath().contains("/auth")) { // Este filtro solo se va a indicar para /auth
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION); // Obtener la authentification del header
        // Si es de tipo Bearer entonces es que no está bien
        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }
        final String jwtToken = authHeader.substring(7); // Obtener token
        final String userEmail = jwtService.extractEmail(jwtToken); // Obtener email desde el token

        //Si es nulo o no hay alguien autentificado actualmente
        if (userEmail == null || SecurityContextHolder.getContext().getAuthentication() != null) return;

        final Token token = tokenRepository.findByToken(jwtToken).orElse(null); // Encontrar el token en la base de datos
        if (token == null || token.isExpired() || token.isRevoked()) { // Se mira si está expirado o revocado
            filterChain.doFilter(request, response);
            return; // Se sigue con lo demás
        }

        final UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail); // Obtener userDetails
        final Optional<User> user = userRepository.findByEmail(userDetails.getUsername()); // Obtener usuario en base al email
        if (user.isEmpty()) { // Si el usuario está vacío es que no existe en la bd
            filterChain.doFilter(request, response);
            return; // Se sigue con lo demás
        }

        final boolean isTokenValid = jwtService.isTokenValid(jwtToken, user.get());
        if (!isTokenValid) {
            return; // Si el token no es válido se sigue con los otros filtros
        }

        // Autentificar en el contexto de spring al usuario
        final UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        // Autentificar dando al token los detalles
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken); // Asignar la nueva autentificación

        filterChain.doFilter(request, response); // Finalmente dar lugar al siguiente filtro
    }
}
