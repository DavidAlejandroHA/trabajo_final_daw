package com.canarycode.appointments.servicios;

import com.canarycode.appointments.dto.jwt.LoginRequest;
import com.canarycode.appointments.dto.jwt.RegisterRequest;
import com.canarycode.appointments.dto.jwt.TokenResponse;
import com.canarycode.appointments.model.Token;
import com.canarycode.appointments.model.User;
import com.canarycode.appointments.repository.TokenRepository;
import com.canarycode.appointments.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService; // Para generar un token y acceder a los recursos de este usuario
    private final AuthenticationManager authenticationManager; // Para facilitar la autentificación de usuarios

    public TokenResponse register(RegisterRequest request) {
        //var user = User.builder()
        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));

        User savedUser = userRepository.save(user);
        String jwtToken = jwtService.generateToken(savedUser); // Token de acceso
        String refreshToken = jwtService.generateRefreshToken(savedUser); // Token de refresco
        saveUserToken(savedUser, jwtToken);// Guardar tokens en la bd
        return new TokenResponse(jwtToken, refreshToken);
    }

    public TokenResponse login(LoginRequest request){
        authenticationManager.authenticate( // Verificar la autenticación usando los datos de la base de datos
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        User user = userRepository.findByEmail(request.email()).orElseThrow(); // Buscar al usuario
        //Tokens
        String token = jwtService.generateToken(user); // Generar un nuevo token de acceso
        String refreshToken = jwtService.generateRefreshToken(user); // Generar un nuevo token de refresco
        revokeAllUserTokens(user); // Expirar y revocar tokens antiguos
        saveUserToken(user, token); // Guardar el nuevo token generado en la base de datos
        return new TokenResponse(token, refreshToken);
    }

    // Para actualizar los tokens de un usuario y marcarlos como expirados y revocados en la base de datos
    private void revokeAllUserTokens(final User user) {
        final List<Token> validUserTokens = tokenRepository.findAllValidTokensByUserId(user.getId()); // Se obtienen todos los tokens de un usuario
        if (!validUserTokens.isEmpty()){  // Si hay tokens en la lista se procede
            for (final Token token : validUserTokens) { // Marcar como expirados y revocados los tokens de la lista
                token.setExpired(true);
                token.setRevoked(true);
            }
            tokenRepository.saveAll(validUserTokens); // Se actualizan todos los tokens del usuario
        }
    }

    // Para refrescar el token
    public TokenResponse refreshToken(final String authHeader){
        if (authHeader == null || !authHeader.startsWith("Bearer ")) { // Verificar el token (Si empieza por "Bearer " entonces es válido)
            throw new IllegalArgumentException("Invalid Bearer Token");
        }

        final String refreshToken = authHeader.substring(7); // Obtener el token sin la palabra "Bearer "
        // Verificar si este token contiene el email (o nombre de usuario si se especificase en otros casos)
        final String userEmail = jwtService.extractEmail(refreshToken); // Obtener el email

        if (userEmail == null) {
            throw new IllegalArgumentException("Invalid Bearer Token");
        }

        // Si el email obtenido no existe se arroja un error
        final User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException(userEmail));

        // Si el email no existe en la base de datos se verifica que el token es inválido
        if (!jwtService.isTokenValid(refreshToken, user)) {
            throw new UsernameNotFoundException(userEmail);
        }

        final String accessToken = jwtService.generateToken(user); // Generar nuevo token de acceso
        revokeAllUserTokens(user); // Eliminar los otros tokens anteriores del usuario
        saveUserToken(user, accessToken); // Guardar los nuevos tokens del usuario
        return new TokenResponse(accessToken, refreshToken);
    }

    // Guardar token de un usuario en la base de datos
    private void saveUserToken(User user, String jwtToken) {
        Token token = new Token();
        token.setUser(user);
        token.setToken(jwtToken);
        token.setTokenType(Token.TokenType.BEARER);
        token.setExpired(false);
        token.setRevoked(false);

        tokenRepository.save(token);
    }
}
