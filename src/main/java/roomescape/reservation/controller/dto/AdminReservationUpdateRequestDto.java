package roomescape.reservation.controller.dto;

import jakarta.validation.constraints.NotNull;

public record AdminReservationUpdateRequestDto(@NotNull Long timeId) {}