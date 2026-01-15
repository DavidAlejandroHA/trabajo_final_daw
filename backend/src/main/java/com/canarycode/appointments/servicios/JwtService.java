package com.canarycode.appointments.servicios;


import com.canarycode.appointments.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    @Value("${application.security.jwt.secret}")
    private String secretKey;
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration; // En milisegundos
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    public String generateToken(User user) { // Generar token de acceso
        return buildToken(user, jwtExpiration);
    }

    public String generateRefreshToken(User user) { // Generar token de refresco
        return buildToken(user, jwtExpiration);
    }

    private String buildToken(User user, long expiration) {
        return Jwts.builder()
                .id(user.getId().toString()) // Id para el token (opcional)
                .claims(Map.of("name", user.getUsername())) // Información adicional para dar a la clave (opcional)
                .subject(user.getUsername()) // Como será identificado el token al usuario
                .issuedAt(new Date(System.currentTimeMillis())) // Cuándo se creó la clave
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), Jwts.SIG.HS256) // Uso del algoritmo HS256 para la firma del algoritmo
                .compact(); // Fin del builder

    }

    // Método_ para generar una clave privada con la clave secreta
    private SecretKey getSignInKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes()); // Uso del algoritmo hmac
    }
}
