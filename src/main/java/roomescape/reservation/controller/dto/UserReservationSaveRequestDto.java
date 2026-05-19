package roomescape.reservation.controller.dto;

import jakarta.validation.constraints.NotNull;

import roomescape.reservation.service.dto.ReservationSaveServiceDto;

public record UserReservationSaveRequestDto(
        @NotNull Long themeId,
        @NotNull Long timeId
) {

    public ReservationSaveServiceDto toServiceDto(Long memberId) {
        return new ReservationSaveServiceDto(memberId, themeId, timeId);
    }
}