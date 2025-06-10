package com.lamashkevich.lotservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lamashkevich.lotservice.entity.*;

import java.time.LocalDateTime;

public record LotResponseDto(
        Long id,
        Integer lotNumber,
        AuctionType auction,
        LotType type,
        String make,
        String model,
        Integer year,
        String vin,
        OdometerResponseDto odometer,
        String engine,
        FuelType fuel,
        TransmissionType transmission,
        DriveType drive,
        Boolean key,
        String damage,
        String title,
        @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
        LocalDateTime auctionDate,
        @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
        LocalDateTime updatedAt
) {
}
