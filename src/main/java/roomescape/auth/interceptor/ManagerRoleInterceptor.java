package roomescape.auth.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;

import roomescape.auth.exception.ForbiddenException;
import roomescape.member.domain.Role;

public class ManagerRoleInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        Role role = (Role) request.getAttribute("role");
        if (role != Role.MANAGER) {
            throw new ForbiddenException();
        }
        return true;
    }
}