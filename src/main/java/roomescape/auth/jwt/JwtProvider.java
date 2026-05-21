package roomescape.auth.jwt;

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
import roomescape.member.domain.Role;

@Component
public class JwtProvider {
    private final SecretKey secretKey;

    public JwtProvider(@Value("${jwt.secretKey}") String secretKey) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String generate(Long id, Role role) {
        Date expiredDate = Date.from(Instant.now().plus(1, ChronoUnit.HOURS));
        return Jwts.builder()
                .claim("id", id)
                .claim("role", role.name())
                .expiration(expiredDate)
                .signWith(secretKey)
                .compact();
    }

    public Long getId(String jwt) {
        return parseClaims(jwt).get("id", Long.class);
    }

    public Role getRole(String jwt) {
        String role = parseClaims(jwt).get("role", String.class);
        return Role.valueOf(role);
    }

    private Claims parseClaims(String jwt) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(jwt)
                    .getPayload();
        } catch (SecurityException | MalformedJwtException | ExpiredJwtException e) {
            throw new AuthenticationException();
        } catch (Exception e) {
            throw new AuthenticationException();
        }
    }
}