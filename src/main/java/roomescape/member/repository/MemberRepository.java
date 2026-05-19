package roomescape.member.repository;

import java.util.Optional;

import roomescape.member.domain.Member;

public interface MemberRepository {
    Optional<Member> findById(Long id);
    Optional<Member> findByEmail(String email);
    Member save(Member member);
}