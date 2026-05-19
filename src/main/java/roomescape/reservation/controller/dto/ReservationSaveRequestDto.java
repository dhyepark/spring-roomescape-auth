package roomescape.reservation.controller.dto;

import jakarta.validation.constraints.NotNull;

import roomescape.reservation.service.dto.ReservationSaveServiceDto;

public record ReservationSaveRequestDto(
        @NotNull Long memberId,
        @NotNull Long themeId,
        @NotNull Long timeId
) {

    public ReservationSaveServiceDto toServiceDto() {
        return new ReservationSaveServiceDto(memberId, themeId, timeId);
    }
}