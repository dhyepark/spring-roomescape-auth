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
import roomescape.reservation.controller.dto.AdminReservationUpdateRequestDto;
import roomescape.reservation.controller.dto.ReservationResponseDto;
import roomescape.reservation.controller.dto.ReservationSaveRequestDto;
import roomescape.reservation.service.AdminReservationService;

@RestController
@RequestMapping("/admin/reservations")
public class AdminReservationController {
    private final AdminReservationService adminReservationService;

    public AdminReservationController(AdminReservationService adminReservationService) {
        this.adminReservationService = adminReservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponseDto>> getAll(@LoginMember Member loginMember) {
        List<ReservationResponseDto> body = adminReservationService.getByManagerId(loginMember.getId()).stream()
                .map(ReservationResponseDto::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(body);
    }

    @PostMapping
    public ResponseEntity<ReservationResponseDto> create(
            @RequestBody @Valid ReservationSaveRequestDto reservationRequest) {
        ReservationResponseDto body = ReservationResponseDto.from(
                adminReservationService.create(reservationRequest.toServiceDto()));
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancel(@PathVariable Long id, @LoginMember Member loginMember) {
        adminReservationService.cancel(id, loginMember.getId());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationResponseDto> update(
            @PathVariable Long id,
            @RequestBody @Valid AdminReservationUpdateRequestDto request,
            @LoginMember Member loginMember) {
        ReservationResponseDto body = ReservationResponseDto.from(
                adminReservationService.update(id, request.timeId(), loginMember.getId()));
        return ResponseEntity.ok(body);
    }
}
