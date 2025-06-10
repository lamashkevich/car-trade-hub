package com.lamashkevich.lotservice.dto;

import com.lamashkevich.lotservice.entity.OdometerStatus;
import com.lamashkevich.lotservice.entity.OdometerUnit;

public record OdometerResponseDto(
        Integer value,
        OdometerUnit unit,
        OdometerStatus status
) {
}
