package com.lamashkevich.lotservice.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LotFilterTest {

    @Test
    public void whenPageAndSizeIsNotValid() {
        LocalDate today = LocalDate.now();
        LocalDate auctionDateFrom = null;
        LocalDate auctionDateTo = null;

        var filter = new LotFilter(
                null, null,
                null, null, null, null,
                null, null, null, null,
                null, null, null, null,
                null, auctionDateFrom, auctionDateTo, null,
                null, null, null
        );

        assertEquals(today.plusMonths(1), filter.auctionDateTo());
        assertEquals(today.minusMonths(1), filter.auctionDateFrom());
    }
}