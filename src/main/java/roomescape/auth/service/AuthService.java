package roomescape.auth.service;

import org.springframework.stereotype.Service;

import roomescape.auth.crypto.BCryptEncryptor;
import roomescape.auth.controller.dto.LoginRequestDto;
import roomescape.auth.exception.AuthenticationException;
import roomescape.auth.jwt.JwtProvider;
import roomescape.member.domain.Member;
import roomescape.member.repository.MemberRepository;

@Service
public class AuthService {
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private final BCryptEncryptor encryptor;

    public AuthService(MemberRepository memberRepository, JwtProvider jwtProvider, BCryptEncryptor encryptor) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
        this.encryptor = encryptor;
    }

    public String login(LoginRequestDto request) {
        Member member = memberRepository.findByEmail(request.email())
                .orElseThrow(AuthenticationException::new);
        if (!encryptor.matches(request.password(), member.getPassword())) {
            throw new AuthenticationException();
        }
        return jwtProvider.generate(member.getId());
    }
}
