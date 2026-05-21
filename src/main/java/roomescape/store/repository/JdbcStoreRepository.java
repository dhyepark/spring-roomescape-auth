package roomescape.store.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import roomescape.store.domain.Store;
import roomescape.store.exception.StoreNotFoundException;

@Repository
public class JdbcStoreRepository implements StoreRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcStoreRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Store> findByManagerId(Long managerId) {
        List<Store> stores = jdbcTemplate.query(
                "SELECT s.id, s.name, s.manager_id FROM store s WHERE s.manager_id = ?",
                new StoreRowMapper(),
                managerId
        );
        return stores.stream().findFirst();
    }

    @Override
    public Store findById(Long id) {
        List<Store> stores = jdbcTemplate.query(
                "SELECT s.id, s.name, s.manager_id FROM store s WHERE s.id = ?",
                new StoreRowMapper(),
                id
        );
        if (stores.isEmpty()) {
            throw new StoreNotFoundException(id);
        }
        return stores.get(0);
    }

    private static class StoreRowMapper implements RowMapper<Store> {
        @Override
        public Store mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Store(
                    rs.getString("name"),
                    rs.getLong("manager_id")
            ).withId(rs.getLong("id"));
        }
    }
}