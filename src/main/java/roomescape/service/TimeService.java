package roomescape.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.dao.TimeDAO;
import roomescape.domain.Time;

import java.util.List;

@Service
public class TimeService {

    @Autowired
    private final TimeDAO timeDAO;

    public TimeService(TimeDAO timeDAO) {
        this.timeDAO = timeDAO;
    }

    public List<Time> getAllTimes() {
        return timeDAO.findAllTime();
    }

    public Time createTime(Time time) {
        return timeDAO.insert(time);
    }

    public void deleteTime(Long id) {
        timeDAO.delete(id);
    }
}
