package roomescape.auth;

import java.util.Arrays;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;

import roomescape.auth.exception.AuthenticationException;

public class LoginCheckInterceptor implements HandlerInterceptor {
    private final JwtProvider jwtProvider;

    public LoginCheckInterceptor(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            throw new AuthenticationException();
        }

        String token = Arrays.stream(cookies)
                .filter(c -> c.getName().equals("token"))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(AuthenticationException::new);

        Long memberId = jwtProvider.getId(token);
        request.setAttribute("memberId", memberId);
        return true;
    }
}

