package roomescape.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import roomescape.exception.InvalidReservationException;
import roomescape.exception.NotFoundReservationException;
import roomescape.model.Reservation;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Controller
public class ReservationController {

    private List<Reservation> reservations = new ArrayList<>();

    private AtomicLong index = new AtomicLong(1);

    @GetMapping("/reservation")
    public String reservation() {
        return "reservation";
    }

    @GetMapping("/reservations")
    @ResponseBody
    public List<Reservation> getReservations() {
        return reservations;
    }

    @PostMapping("/reservations")
    @ResponseBody
    public ResponseEntity<Reservation> createReservation(@RequestBody Reservation reservation) {
        validateReservation(reservation);

        Long id = index.getAndIncrement();
        String name = reservation.getName();
        String date = reservation.getDate();
        String time = reservation.getTime();

        Reservation newReservation = new Reservation(id, name, date, time);
        reservations.add(newReservation);

        return ResponseEntity.created(URI.create("/reservations/" + newReservation.getId())).body(newReservation);
    }

    @DeleteMapping("/reservations/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        boolean removed = reservations.removeIf(r -> r.getId() == id);
        if (!removed) {
            throw new NotFoundReservationException("예약을 찾을 수 없습니다: " + id);
        }
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler({InvalidReservationException.class, NotFoundReservationException.class})
    public ResponseEntity<String> handleException(RuntimeException e, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    private void validateReservation(Reservation reservation) {
        if (reservation.getName() == null || reservation.getName().isEmpty()) {
                throw new InvalidReservationException("이름이 필요합니다.");
            }
        if (reservation.getDate() == null || reservation.getDate().isEmpty()) {
                throw new InvalidReservationException("날짜가 필요합니다.");
            }
        if (reservation.getTime() == null || reservation.getTime().isEmpty()) {
                throw new InvalidReservationException("시간이 필요합니다");
            }
    }
}
