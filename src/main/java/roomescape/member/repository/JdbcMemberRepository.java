package roomescape.member.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import roomescape.member.domain.Member;

@Repository
public class JdbcMemberRepository implements MemberRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert memberInsert;

    public JdbcMemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.memberInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Optional<Member> findById(Long id) {
        List<Member> results = jdbcTemplate.query(
                "SELECT id, email, password, name FROM member WHERE id = ?",
                new MemberRowMapper(),
                id
        );
        return results.stream().findFirst();
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        List<Member> results = jdbcTemplate.query(
                "SELECT id, email, password, name FROM member WHERE email = ?",
                new MemberRowMapper(),
                email
        );
        return results.stream().findFirst();
    }

    @Override
    public Member save(Member member) {
        Number id = memberInsert.executeAndReturnKey(new MapSqlParameterSource()
                .addValue("email", member.getEmail())
                .addValue("password", member.getPassword())
                .addValue("name", member.getName()));
        return member.withId(id.longValue());
    }

    private static class MemberRowMapper implements RowMapper<Member> {
        @Override
        public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Member(
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("name")
            ).withId(rs.getLong("id"));
        }
    }
}