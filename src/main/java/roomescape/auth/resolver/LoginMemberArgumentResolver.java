package roomescape.auth.resolver;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import roomescape.auth.exception.AuthenticationException;
import roomescape.member.domain.Member;
import roomescape.member.repository.MemberRepository;

public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberRepository memberRepository;

    public LoginMemberArgumentResolver(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasAnnotation = parameter.hasParameterAnnotation(LoginMember.class);
        boolean isMemberType = Member.class.isAssignableFrom(parameter.getParameterType());
        return hasAnnotation && isMemberType;
    }

    @Nullable
    @Override
    public Object resolveArgument(@NonNull MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if (request == null) {
            throw new AuthenticationException();
        }

        Long memberId = (Long) request.getAttribute("memberId");
        if (memberId == null) {
            throw new AuthenticationException();
        }

        return memberRepository.findById(memberId)
                .orElseThrow(AuthenticationException::new);
    }
}