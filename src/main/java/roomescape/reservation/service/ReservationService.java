package roomescape.reservation.service;

import java.util.List;

import roomescape.reservation.domain.Reservation;
import roomescape.reservation.service.dto.ReservationSaveServiceDto;

public interface ReservationService {
    List<Reservation> getByMemberId(Long memberId);
    Reservation create(ReservationSaveServiceDto dto);
    void cancel(Long id, Long memberId);
    Reservation update(Long id, Long timeId, Long memberId);
}
