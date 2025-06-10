package com.lamashkevich.lotservice.dto;

import com.lamashkevich.lotservice.entity.OdometerStatus;
import com.lamashkevich.lotservice.entity.OdometerUnit;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OdometerRequestDto(
        @Min(value = 0, message = "Odometer value be at least 0")
        @NotNull(message = "Odometer value is required")
        Integer value,

        @NotNull(message = "Odometer unit is required")
        OdometerUnit unit,

        @NotNull(message = "Odometer status is required")
        OdometerStatus status
) {
}
