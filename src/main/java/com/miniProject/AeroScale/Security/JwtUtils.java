package com.miniProject.AeroScale.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Slf4j
public class JwtUtils {

    @Value("${app.jwt.secret}")
    private String base64Secret;

    @Value("${app.jwt.access-token-expiration-ms}")
    private long accessTokenExpirationMs;

    @Value("${app.jwt.issuer}")
    private String issuer;

    private SecretKey signingKey;

    @PostConstruct
    void init() {
        byte[] keyBytes = java.util.Base64.getDecoder().decode(base64Secret);
        if (keyBytes.length < 32) {
            throw new IllegalStateException("app.jwt.secret must decode to at least 256 bits for HS256");
        }
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(UUID userId, String email, String role) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + accessTokenExpirationMs);

        return Jwts.builder()
                .subject(userId.toString())
                .claim("email", email)
                .claim("role", role)
                .issuer(issuer)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(signingKey, Jwts.SIG.HS256)
                .compact();
    }

    public long getAccessTokenExpirationMs() {
        return accessTokenExpirationMs;
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .requireIssuer(issuer)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public UUID extractUserId(String token) {
        return UUID.fromString(extractAllClaims(token).getSubject());
    }

    public String extractEmail(String token) {
        return extractAllClaims(token).get("email", String.class);
    }

    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }


    public boolean isTokenValid(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.debug("JWT expired: {}", e.getMessage());
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Invalid JWT: {}", e.getMessage());
        }
        return false;
    }
}
