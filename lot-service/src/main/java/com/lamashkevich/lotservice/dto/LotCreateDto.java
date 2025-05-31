package com.lamashkevich.lotservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lamashkevich.lotservice.entity.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record LotCreateDto(
        @NotNull(message = "Lot number is required")
        Integer lotNumber,

        @NotNull(message = "Auction type is required")
        AuctionType auction,

        @NotNull(message = "Lot type is required")
        LotType type,

        String make,

        String model,

        @Min(value = 1900, message = "Year must be at least 1900")
        Integer year,

        @NotBlank(message = "VIN is required")
        @Size(min = 17, max = 17, message = "VIN must be exactly 17 characters")
        String vin,

        @Valid
        OdometerRequestDto odometer,

        @Size(min = 3, max = 20, message = "Engine must be between 3 and 20 characters")
        String engine,

        FuelType fuel,

        TransmissionType transmission,

        DriveType drive,

        Boolean key,

        @Size(min = 3, message = "Damage must contain more than 3 characters")
        String damage,

        @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
        String title,

        @FutureOrPresent(message = "Auction date cannot be in the past")
        @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
        LocalDateTime auctionDate
) {
}
