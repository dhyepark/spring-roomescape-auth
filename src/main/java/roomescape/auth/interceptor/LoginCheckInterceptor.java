package roomescape.auth.interceptor;

import java.util.Arrays;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;

import roomescape.auth.exception.AuthenticationException;
import roomescape.auth.jwt.JwtProvider;

public class LoginCheckInterceptor implements HandlerInterceptor {
    private final JwtProvider jwtProvider;

    public LoginCheckInterceptor(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        String token = extractToken(request);
        Long memberId = jwtProvider.getId(token);
        request.setAttribute("memberId", memberId);
        return true;
    }

    private String extractToken(HttpServletRequest request) {
        String token = extractFromCookie(request);
        if (token != null) {
            return token;
        }
        return extractFromAuthorizationHeader(request);
    }

    private String extractFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        return Arrays.stream(cookies)
                .filter(c -> c.getName().equals("token"))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }

    private String extractFromAuthorizationHeader(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            throw new AuthenticationException();
        }
        return header.substring(7);
    }
}
