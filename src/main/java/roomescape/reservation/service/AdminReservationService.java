package roomescape.reservation.service;

import java.util.List;

import roomescape.reservation.domain.Reservation;
import roomescape.reservation.service.dto.ReservationSaveServiceDto;

public interface AdminReservationService {
    List<Reservation> getByManagerId(Long managerId);
    Reservation create(ReservationSaveServiceDto dto);
    void cancel(Long id, Long managerId);
    Reservation update(Long id, Long timeId, Long managerId);
}
