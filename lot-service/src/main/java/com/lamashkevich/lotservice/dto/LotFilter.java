package com.lamashkevich.lotservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lamashkevich.lotservice.entity.*;

import java.time.LocalDate;

public record LotFilter(
        Integer lotNumber,
        AuctionType auction,
        LotType type,
        String make,
        String model,
        Integer yearFrom,
        Integer yearTo,
        String vin,
        String engine,
        FuelType fuel,
        TransmissionType transmission,
        DriveType drive,
        Boolean key,
        String damage,
        String title,

        @JsonFormat(pattern = "dd-MM-yyyy")
        LocalDate auctionDateFrom,

        @JsonFormat(pattern = "dd-MM-yyyy")
        LocalDate auctionDateTo,

        Integer odometerValueFrom,
        Integer odometerValueTo,
        OdometerUnit odometerUnit,
        OdometerStatus odometerStatus
) {
    public LotFilter {
        if (auctionDateFrom == null) {
            auctionDateFrom = LocalDate.now().minusMonths(1);
        }
        if (auctionDateTo == null) {
            auctionDateTo = LocalDate.now().plusMonths(1);
        }
    }
}
