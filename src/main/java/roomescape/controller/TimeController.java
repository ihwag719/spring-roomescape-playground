package roomescape.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.domain.Time;
import roomescape.service.TimeService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/times")
public class TimeController {

    @Autowired
    private final TimeService timeService;
    public TimeController(TimeService timeService) {
        this.timeService = timeService;
    }

    @GetMapping
    public List<Time> getTimes() {
        return timeService.getAllTimes();
    }

    @PostMapping
    public ResponseEntity<Time> createTime(@RequestBody Time time) {
        Time newTime = timeService.createTime(time);
        return ResponseEntity.created((URI.create("/times/" + newTime.getId()))).body(newTime);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTime(@PathVariable Long id) {
        timeService.deleteTime(id);
        return ResponseEntity.noContent().build();
    }
}
