package roomescape.reservation.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;

public interface ReservationRepository {
    List<Reservation> findAll();
    Optional<Reservation> findById(Long id);
    List<Reservation> findAllByMemberId(Long memberId);
    List<Reservation> findAllByStoreId(Long storeId);
    Reservation save(Reservation reservation);
    boolean update(Long id, Long timeId);
    List<Long> findTimeIdsByThemeIdAndDate(Long themeId, LocalDate date);
    boolean deleteById(Long id);
    boolean isDuplicated(Long themeId, ReservationTime time);
    boolean existsByTimeId(Long timeId);
}
