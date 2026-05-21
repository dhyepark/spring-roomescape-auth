package roomescape.reservation.controller;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import roomescape.auth.resolver.LoginMember;
import roomescape.member.domain.Member;
import roomescape.reservation.controller.dto.ReservationResponseDto;
import roomescape.reservation.controller.dto.UserReservationSaveRequestDto;
import roomescape.reservation.controller.dto.UserReservationUpdateRequestDto;
import roomescape.reservation.service.ReservationService;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponseDto>> getMyReservations(@LoginMember Member loginMember) {
        List<ReservationResponseDto> body = reservationService.getByMemberId(loginMember.getId()).stream()
                .map(ReservationResponseDto::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(body);
    }

    @PostMapping
    public ResponseEntity<ReservationResponseDto> create(
            @RequestBody @Valid UserReservationSaveRequestDto request,
            @LoginMember Member loginMember) {
        ReservationResponseDto body = ReservationResponseDto.from(
                reservationService.create(request.toServiceDto(loginMember.getId())));
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancel(@PathVariable Long id, @LoginMember Member loginMember) {
        reservationService.cancel(id, loginMember.getId());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationResponseDto> update(
            @PathVariable Long id,
            @RequestBody @Valid UserReservationUpdateRequestDto request,
            @LoginMember Member loginMember) {
        ReservationResponseDto body = ReservationResponseDto.from(
                reservationService.update(id, request.timeId(), loginMember.getId()));
        return ResponseEntity.ok(body);
    }
}
