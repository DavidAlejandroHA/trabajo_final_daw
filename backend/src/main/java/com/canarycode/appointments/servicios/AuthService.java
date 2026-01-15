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

    public AuthService(UserRepository userRepository, TokenRepository tokenRepository, PasswordEncoder passwordEncoder, JwtService jwtService,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public TokenResponse register(RegisterRequest request) {
        //var user = User.builder()
        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));

        User savedUser = userRepository.save(user);
        String jwtToken = jwtService.generateToken(user); // Token de acceso
        String refreshToken = jwtService.generateRefreshToken(user); // Token de refresco
        saveUserToken(user, jwtToken);// Guardar tokens en la bd
        return new TokenResponse(jwtToken, refreshToken);
    }

    public TokenResponse login(LoginRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        User user = userRepository.findByEmail(request.email()).orElseThrow(); // Buscar al usuario
        String token = jwtService.generateToken(user); // Generar un nuevo token de acceso
        String refreshToken = jwtService.generateRefreshToken(user); // Generar un nuevo token de refresco
        revokeAllUserTokens(user);
        return TokenResponse()
    }

    private void revokeAllUserTokens(final User user) {
        final List<Token> validUserTokens = tokenRepository.
    }

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
