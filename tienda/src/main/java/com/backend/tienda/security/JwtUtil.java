package com.backend.tienda.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // Aquí inyectamos la propiedad que definiste en application.properties
    @Value("${jwt.secret}")
    private String secret;

    private Key key;

    @PostConstruct
    public void init() {
        // Convertimos tu texto secreto en una llave criptográfica
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    // Generar Token para el usuario
    public String generateToken(String correo, String role) {
        return Jwts.builder()
                .setSubject(correo)
                .claim("role", role) // Guardamos el rol dentro del token
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 horas
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Validar si el token es correcto
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // Obtener el correo desde el token
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}