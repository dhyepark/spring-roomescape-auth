package roomescape.auth.service;

import org.springframework.stereotype.Service;

import roomescape.auth.controller.dto.LoginRequestDto;
import roomescape.auth.exception.AuthenticationException;
import roomescape.auth.jwt.JwtProvider;
import roomescape.member.domain.Member;
import roomescape.member.repository.MemberRepository;

@Service
public class AuthService {
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    public AuthService(MemberRepository memberRepository, JwtProvider jwtProvider) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
    }

    public String login(LoginRequestDto request) {
        Member member = memberRepository.findByEmail(request.email())
                .orElseThrow(AuthenticationException::new);
        if (!member.getPassword().equals(request.password())) {
            throw new AuthenticationException();
        }
        return jwtProvider.generate(member.getId());
    }
}