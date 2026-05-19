package roomescape.member.service;

import roomescape.member.domain.Member;

public interface MemberService {
    Member findById(Long id);
    Member findByEmail(String email);
}