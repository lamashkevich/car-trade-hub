package com.lamashkevich.lotservice.service;

import com.lamashkevich.lotservice.dto.LotCreateDto;
import com.lamashkevich.lotservice.dto.OdometerRequestDto;
import com.lamashkevich.lotservice.entity.*;
import com.lamashkevich.lotservice.exception.LotAlreadyExistsException;
import com.lamashkevich.lotservice.exception.LotNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@Sql("/sql/init.sql")
@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class LotServiceTest {

    @Container
    public static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17.4");

    private static final Long EXISING_ID = 1L;
    private static final Long NOT_EXISING_ID = 999L;
    private static final Integer EXISING_LOT_NUMBER = 12345678;
    private static final Integer NOT_EXISING_LOT_NUMBER = 99999999;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private LotService lotService;

    @Test
    void findById_whenLotIsFound() {
        var result = lotService.findById(EXISING_ID);

        assertNotNull(result);
        assertEquals(EXISING_ID, result.id());
    }

    @Test
    void findById_whenLotIsNotFound() {
        assertThrows(LotNotFoundException.class, () -> lotService.findById(NOT_EXISING_ID));
    }

    @Test
    void create_whenLotDoesNotExist() {
        var lotCreateDto = getLotCreateDto(NOT_EXISING_LOT_NUMBER);

        var result = lotService.create(lotCreateDto);

        assertNotNull(result);
        assertNotNull(result.id());
        assertEquals(lotCreateDto.lotNumber(), result.lotNumber());
        assertEquals(lotCreateDto.auction(), result.auction());
        assertEquals(lotCreateDto.type(), result.type());
        assertEquals(lotCreateDto.make(), result.make());
        assertEquals(lotCreateDto.model(), result.model());
        assertEquals(lotCreateDto.year(), result.year());
        assertEquals(lotCreateDto.vin(), result.vin());
        assertEquals(lotCreateDto.odometer().value(), result.odometer().value());
        assertEquals(lotCreateDto.odometer().unit(), result.odometer().unit());
        assertEquals(lotCreateDto.odometer().status(), result.odometer().status());
       assertEquals(lotCreateDto.engine(), result.engine());
        assertEquals(lotCreateDto.fuel(), result.fuel());
        assertEquals(lotCreateDto.transmission(), result.transmission());
        assertEquals(lotCreateDto.drive(), result.drive());
        assertEquals(lotCreateDto.key(), result.key());
        assertEquals(lotCreateDto.damage(), result.damage());
        assertEquals(lotCreateDto.title(), result.title());
        assertEquals(lotCreateDto.auctionDate(), result.auctionDate());
        assertNotNull(result.updatedAt());
    }

    @Test
    void create_whenLotAlreadyExists() {
        var lotCreateDto = getLotCreateDto(EXISING_LOT_NUMBER);

        assertThrows(LotAlreadyExistsException.class, () -> lotService.create(lotCreateDto));
    }

    @Test
    void deleteById_whenLotIsFound() {
        var result = lotService.deleteById(EXISING_ID);

        assertNotNull(result);
        assertEquals(EXISING_ID, result.id());
    }

    @Test
    void deleteById_whenLotIsNotFound() {
        assertThrows(LotNotFoundException.class, () -> lotService.deleteById(NOT_EXISING_ID));
    }

    private LotCreateDto getLotCreateDto(Integer lotNumber) {
        return new LotCreateDto(
                lotNumber,
                AuctionType.COPART,
                LotType.CAR,
                "Ford",
                "Mustang",
                2025,
                "0123456789ABC1234",
                new OdometerRequestDto(1234, OdometerUnit.MILES, OdometerStatus.ACTUAL),
                "3.0, 372HP",
                FuelType.GASOLINE,
                TransmissionType.AUTOMATIC,
                DriveType.AWD,
                true,
                "unknown",
                "unknown",
                LocalDateTime.now()
        );
    }
}