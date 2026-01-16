package com.canarycode.appointments.security;

import com.canarycode.appointments.model.Token;
import com.canarycode.appointments.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final TokenRepository tokenRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http){
        http
                .csrf(AbstractHttpConfigurer::disable) // Deshabilitar Cross Site Request Forgery (CSRF)
                .authorizeHttpRequests(req ->
                        req.requestMatchers("/api/auth/**").permitAll() // Permitir todos los endpoints que terminen en /auth/ a todos los usuarios
                                //.requestMatchers("/api/admin").hasRole("ADMIN")
                                .anyRequest()
                                .authenticated() // Todas las demás peticiones tienen que ser autentificadas
                )
                // La manera de manerar la sesión será sin tenre ninguna sesión http (STATELES)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider) // De que forma spring puede encontrar al usuario y ver si existe
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) // Verificar con un filtro si el controlador está correcto
                // Antes de comprobar si el usuario está autentificado (por medio del filtro indicado en el 2º parámetro) se dispara el jwtAuthFilter
                .logout(logout ->
                        logout.logoutUrl("/api/auth/logout")
                                .addLogoutHandler((request, response, authentication) -> {
                                    final var authHeader = request.getHeader(HttpHeaders.AUTHORIZATION); // Obtener el header al hacer logout
                                    logout(authHeader);
                                })
                                .logoutSuccessHandler((request, response, authentication) -> {
                                    SecurityContextHolder.clearContext(); // Limpiar el contexto de spring al hacer logout con éxito
                                })
                )
                .httpBasic(Customizer.withDefaults()) // Configurar basic auth
                ;

        // Definición del comportamiento del login
        /*http.formLogin(login -> {
            login.loginPage("/auth/login")
                    .loginProcessingUrl("/auth/login")
                    //.usernameParameter("email")
                    //.passwordParameter("password")
                    .defaultSuccessUrl("/", true)
                    .permitAll();
        });*/

        return http.build();
    }

    // Al hacer logout se pondrá el token usado anteriormente como expirado y revocado
    private void logout(final String token){
        if (token == null || !token.startsWith("Bearer ")){
            throw new IllegalArgumentException("Invalid token");
        }

        final String jwtToken = token.substring(8);
        final Token foundToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));
        foundToken.setExpired(true);
        foundToken.setRevoked(true);
        tokenRepository.save(foundToken);
    }
}
