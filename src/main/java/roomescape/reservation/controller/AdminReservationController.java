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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import roomescape.auth.resolver.LoginMember;
import roomescape.member.domain.Member;
import roomescape.reservation.controller.dto.ReservationResponseDto;
import roomescape.reservation.controller.dto.ReservationSaveRequestDto;
import roomescape.reservation.service.ReservationService;
import roomescape.store.domain.Store;
import roomescape.store.exception.StoreNotFoundException;
import roomescape.store.repository.StoreRepository;

@RestController
@RequestMapping("/admin/reservations")
public class AdminReservationController {
    private final ReservationService reservationService;
    private final StoreRepository storeRepository;

    public AdminReservationController(ReservationService reservationService, StoreRepository storeRepository) {
        this.reservationService = reservationService;
        this.storeRepository = storeRepository;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponseDto>> getAll(@LoginMember Member loginMember) {
        Store store = storeRepository.findByManagerId(loginMember.getId())
                .orElseThrow(() -> new StoreNotFoundException(loginMember.getId()));
        List<ReservationResponseDto> body = reservationService.getByStoreId(store.getId()).stream()
                .map(ReservationResponseDto::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(body);
    }

    @PostMapping
    public ResponseEntity<ReservationResponseDto> create(
            @RequestBody @Valid ReservationSaveRequestDto reservationRequest) {
        ReservationResponseDto body = ReservationResponseDto.from(
                reservationService.create(reservationRequest.toServiceDto()));
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancel(@PathVariable Long id, @LoginMember Member loginMember) {
        Store store = storeRepository.findByManagerId(loginMember.getId())
                .orElseThrow(() -> new StoreNotFoundException(loginMember.getId()));
        reservationService.cancelForManager(id, store.getId());
        return ResponseEntity.noContent().build();
    }
}