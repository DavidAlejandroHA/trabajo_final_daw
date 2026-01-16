package com.canarycode.appointments.servicios;


import com.canarycode.appointments.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration; // En milisegundos
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    public String generateToken(User user) { // Generar token de acceso
        return buildToken(user, jwtExpiration);
    }

    public String generateRefreshToken(User user) { // Generar token de refresco
        return buildToken(user, refreshExpiration);
    }

    private String buildToken(User user, long expiration) {

        return Jwts.builder()
                .id(user.getId().toString()) // Id para el token (opcional)
                .claims(Map.of("name", user.getUsername())) // Información adicional para dar a la clave (opcional)
                .subject(user.getUsername()) // Como será identificado el token al usuario
                .issuedAt(new Date(System.currentTimeMillis())) // Cuándo se creó la clave
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), Jwts.SIG.HS256) // Uso del algoritmo HS256 para la firma
                .compact(); // Fin del builder

    }

    // Método_ para generar una clave privada con la clave secreta
    /*private SecretKey getSignInKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)); // Uso del algoritmo hmac
    }*/
    private SecretKey getSignInKey() {
        String signingKeyB64= Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
        //final byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        final byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }



    // Comprobar si el token es válido en base al email del user y que no esté expirado
    public boolean isTokenValid(String token, User user) {
        final String email = extractEmail(token);
        return email.equals(user.getEmail()) && !isTokenExpired(token);
    }

    // Comprobar si el token está expirado
    private boolean isTokenExpired(final String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extraer email del token
    public String extractEmail(String token) {
        final Claims jwtToken = Jwts.parser()
                .verifyWith(getSignInKey()) // Se verifica con la clave privada
                .build()
                .parseSignedClaims(token) // Se parsea el token
                .getPayload(); // Obtener la información que contiene el token
        return jwtToken.getSubject();
    }

    // Extraer fecha de expiración del token
    private Date extractExpiration(String token) {
        final Claims jwtToken = Jwts.parser()
                .verifyWith(getSignInKey()) // Se verifica con la clave privada
                .build()
                .parseSignedClaims(token) // Se parsea el token
                .getPayload(); // Obtener la información que contiene el token
        return jwtToken.getExpiration();
    }
}
