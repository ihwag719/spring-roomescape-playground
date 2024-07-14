package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.dto.ReservationRequestDto;
import roomescape.dto.ReservationResponseDto;
import roomescape.exception.ReservationValidator;
import roomescape.model.Reservation;
import roomescape.model.Time;
import roomescape.respository.ReservationRepository;
import roomescape.respository.TimeRepository;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationRepository reservationRepository;
    private final TimeRepository timeRepository;

    public ReservationController(ReservationRepository reservationRepository, TimeRepository timeRepository) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
    }

    @GetMapping
    public List<ReservationResponseDto> getReservations() {
        List<Reservation> reservations = reservationRepository.findAllReservations();
        return reservations.stream()
                .map(reservation -> new ReservationResponseDto(reservation.getId(), reservation.getName(), reservation.getDate(), reservation.getTime()))
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<ReservationResponseDto> createReservation(@RequestBody ReservationRequestDto requestDto) {
        ReservationValidator.validate(requestDto);

        Time time = timeRepository.findTimeByTimeValue(requestDto.getTime());
        if (time == null) {
            time = timeRepository.insert(new Time(null, requestDto.getTime()));
        }

        Reservation reservation = new Reservation(null, requestDto.getName(), requestDto.getDate(), time);
        Reservation newReservation = reservationRepository.insert(reservation);

        ReservationResponseDto responseDto = new ReservationResponseDto(newReservation.getId(), newReservation.getName(), newReservation.getDate(), newReservation.getTime());

        return ResponseEntity.created(URI.create("/reservations/" + newReservation.getId())).body(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationRepository.delete(id);
        return ResponseEntity.noContent().build();
    }
}
