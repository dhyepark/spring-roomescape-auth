package roomescape.reservation.controller.dto;

import roomescape.reservation.domain.Reservation;
import roomescape.theme.controller.dto.ThemeResponseDto;

public record ReservationResponseDto(
        Long id,
        String name,
        ReservationTimeResponseDto time,
        ThemeResponseDto theme
) {

    public static ReservationResponseDto from(Reservation reservation) {
        String memberName = reservation.getMember() != null ? reservation.getMember().getName() : null;
        return new ReservationResponseDto(
                reservation.getId(),
                memberName,
                ReservationTimeResponseDto.from(reservation.getTime()),
                ThemeResponseDto.from(reservation.getTheme())
        );
    }
}