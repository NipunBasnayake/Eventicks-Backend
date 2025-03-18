package edu.icet.eventicks.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {
    private final String secret = "EvEnTiCkSEvEnTiCkSEvEnTiCkSEvEnTiCkSEvEnTiCkSEvEnTiCkSEvEnTiCkS";
    private final long expirationTime = 3600000;

    private final SecretKey signingKey;

    public JwtUtil() {
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes());
        log.info("JwtUtil initialized with signing key");
    }

    public String generateToken(String email) {
        try {
            if (email == null || email.isEmpty()) {
                log.error("Cannot generate token for null or empty email");
                return null;
            }

            log.info("Generating token for email: {}", email);
            Date now = new Date();
            Date expiration = new Date(now.getTime() + expirationTime);

            String token = Jwts.builder()
                    .subject(email)
                    .issuedAt(now)
                    .expiration(expiration)
                    .signWith(signingKey)
                    .compact();

            log.info("Token generated successfully");
            return token;
        } catch (Exception e) {
            log.error("Error generating token: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to generate JWT token", e);
        }
    }

    public String extractEmail(String token) {
        try {
            if (token == null || token.isEmpty()) {
                log.error("Cannot extract email from null or empty token");
                return null;
            }

            Claims claims = Jwts.parser()
                    .verifyWith(signingKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return claims.getSubject();
        } catch (JwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage(), e);
            return null;
        }
    }

    public boolean isTokenValid(String token, String email) {
        if (token == null || email == null) {
            return false;
        }

        String extractedEmail = extractEmail(token);
        if (extractedEmail == null) {
            return false;
        }

        return email.equals(extractedEmail) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(signingKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return claims.getExpiration().before(new Date());
        } catch (JwtException e) {
            log.error("Error checking token expiration: {}", e.getMessage(), e);
            return true;
        }
    }
}