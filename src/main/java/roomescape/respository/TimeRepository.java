package roomescape.respository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.exception.NotFoundReservationException;
import roomescape.model.Reservation;
import roomescape.model.Time;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class TimeRepository {
    private JdbcTemplate jdbcTemplate;

    public TimeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Time> rowMapper = (resultSet, rowNum) -> {
        Time time = new Time(
                resultSet.getLong("id"),
                resultSet.getString("time"));
        return time;
    };

    public List<Time> findAllTime() {
        String sql = "SELECT id, time FROM time";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Time findTimeById(Long id) {
        String sql = "SELECT id, time FROM time where id = ?";
            return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public Time insert(Time time) {

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO time(time) VALUES (?)", new String[]{"id"});
            ps.setString(1, time.getTime());
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();
        return new Time(id, time.getTime());
    }

    public void delete(Long id) {
        String sql = "DELETE FROM time WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
