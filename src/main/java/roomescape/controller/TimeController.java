package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.model.Time;
import roomescape.respository.TimeRepository;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/times")
public class TimeController {
    private final TimeRepository timeRepository;

    public TimeController(TimeRepository timeRepository) {
        this.timeRepository = timeRepository;
    }

    @GetMapping
    @ResponseBody
    public List<Time> getTimes() {
        return timeRepository.findAllTime();
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<Time> createTime(@RequestBody Time time) {
        Time newTime = timeRepository.insert(time);
        return ResponseEntity.created((URI.create("/times/" + newTime.getId()))).body(newTime);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteTime(@PathVariable Long id) {
        timeRepository.delete(id);
        return ResponseEntity.noContent().build();
    }
}
