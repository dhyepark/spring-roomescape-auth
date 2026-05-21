package roomescape.theme.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import roomescape.theme.domain.Theme;
import roomescape.theme.exception.ThemeNotFoundException;

@Repository
public class JdbcThemeRepository implements ThemeRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert themeInsert;

    public JdbcThemeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.themeInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Theme> findAll() {
        return jdbcTemplate.query(
                "SELECT t.id, t.name, t.description, t.image_url, t.store_id FROM theme t",
                new ThemeRowMapper()
        );
    }

    @Override
    public Theme findById(Long id) {
        List<Theme> themes = jdbcTemplate.query(
                "SELECT t.id, t.name, t.description, t.image_url, t.store_id FROM theme t WHERE t.id = ?",
                new ThemeRowMapper(),
                id
        );
        if (themes.isEmpty()) {
            throw new ThemeNotFoundException(id);
        }
        return themes.get(0);
    }

    @Override
    public Theme save(Theme theme) {
        Number id = themeInsert.executeAndReturnKey(new MapSqlParameterSource()
                .addValue("name", theme.getName())
                .addValue("description", theme.getDescription())
                .addValue("image_url", theme.getImageUrl())
                .addValue("store_id", theme.getStoreId()));
        return theme.withId(id.longValue());
    }

    @Override
    public boolean existsById(Long id) {
        Integer exists = jdbcTemplate.queryForObject(
                "SELECT EXISTS(SELECT 1 FROM theme WHERE id = ?)",
                Integer.class,
                id
        );
        return exists != null && exists == 1;
    }

    @Override
    public boolean deleteById(Long id) {
        int affectedRows = jdbcTemplate.update("DELETE FROM theme WHERE id = ?", id);
        return affectedRows > 0;
    }

    @Override
    public List<Theme> findBestThemesByDate(LocalDate startDate, LocalDate endDate, int limit) {
        return jdbcTemplate.query(
                """
                SELECT t.id, t.name, t.description, t.image_url, t.store_id
                FROM theme t
                JOIN reservation r ON r.theme_id = t.id
                JOIN reservation_time rt ON r.time_id = rt.id
                WHERE rt.start_time >= ? AND rt.start_time < ?
                GROUP BY t.id, t.name, t.description, t.image_url
                ORDER BY COUNT(r.id) DESC, t.id ASC
                LIMIT ?
                """,
                new ThemeRowMapper(),
                startDate.atStartOfDay(),
                endDate.plusDays(1).atStartOfDay(),
                limit
        );
    }

    private static class ThemeRowMapper implements RowMapper<Theme> {
        @Override
        public Theme mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Theme(
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getString("image_url"),
                    rs.getLong("store_id")
            ).withId(rs.getLong("id"));
        }
    }
}