package roomescape.reservation.service;

import java.util.List;

import roomescape.reservation.domain.Reservation;
import roomescape.reservation.service.dto.ReservationSaveServiceDto;

public interface ReservationService {
    List<Reservation> getAll();
    Reservation create(ReservationSaveServiceDto reservation);
    void cancel(Long id);
    List<Reservation> getByMemberId(Long memberId);
    List<Reservation> getByStoreId(Long storeId);
    void cancelForUser(Long id, Long memberId);
    void cancelForManager(Long id, Long storeId);
    Reservation update(Long id, Long timeId, Long memberId);
}