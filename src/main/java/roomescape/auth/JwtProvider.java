package roomescape.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import roomescape.auth.exception.AuthenticationException;

@Component
public class JwtProvider {
    private final SecretKey secretKey;

    public JwtProvider(@Value("${jwt.secretKey}") String secretKey) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String generate(Long id) {
        Date expiredDate = Date.from(Instant.now().plus(1, ChronoUnit.HOURS));
        return Jwts.builder()
                .claim("id", id)
                .expiration(expiredDate)
                .signWith(secretKey)
                .compact();
    }

    public Long getId(String jwt) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(jwt)
                    .getPayload();
            return claims.get("id", Long.class);
        } catch (SecurityException | MalformedJwtException | ExpiredJwtException e) {
            throw new AuthenticationException();
        } catch (Exception e) {
            throw new AuthenticationException();
        }
    }
}
