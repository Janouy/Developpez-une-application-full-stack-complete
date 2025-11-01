package com.openclassrooms.mddapi.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

/** Service utilitaire pour générer et valider des JWT (HS256). */
@Service
public class JwtService {

    private final Key key;
    private final long expirationMs;
    private final String issuer;

    /**
     * @param secret clé HMAC (32+ octets recommandés pour HS256)
     * @param expirationMs durée de vie du token en millisecondes
     * @param issuer émetteur attendu (claim {@code iss})
     */
    public JwtService(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.expiration-ms}") long expirationMs,
            @Value("${app.jwt.issuer}") String issuer) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
        this.issuer = issuer;
    }

    /**
     * Générer un JWT signé.
     * @param userId sujet du token (ex: 12)
     * @return token compacté (JWS)
     */
    public String generate(Long userId) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuer(issuer)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extraire le {@code sub} après validation (signature, expiration, issuer).
     * @param token JWT compacté
     * @return l'ID utilisateur extrait du token
     * @throws JwtException si le token est invalide/expiré/mal formé
     * @throws IllegalArgumentException si le token est null/vide
     */
    public Long getUserId(String token) {
        String subject = Jwts.parserBuilder()
                .setSigningKey(key)
                .requireIssuer(issuer)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        return Long.valueOf(subject);
    }

}
