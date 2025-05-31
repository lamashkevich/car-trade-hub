package com.lamashkevich.lotservice.repository.spec;

import com.lamashkevich.lotservice.entity.*;
import com.lamashkevich.lotservice.repository.LotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@Testcontainers
@DataJpaTest(properties = {"spring.test.database.replace=none"})
class LotSpecificationTest {

    @Container
    public static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17.4");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private LotRepository lotRepository;

    @BeforeEach
    void setUp() {
        var odometer1 = Odometer.builder()
                .value(123)
                .unit(OdometerUnit.KILOMETERS)
                .status(OdometerStatus.ACTUAL)
                .build();

        var odometer2 = Odometer.builder()
                .value(9999)
                .unit(OdometerUnit.MILES)
                .status(OdometerStatus.NOT_ACTUAL)
                .build();

        var lot1 = Lot.builder()
                .auction(AuctionType.COPART)
                .lotNumber(12345678)
                .type(LotType.CAR)
                .make("MERCEDES-BENZ")
                .model("A-Class")
                .year(2024)
                .vin("0123456789ABC1234")
                .odometer(odometer1)
                .engine("2.0, 250hp")
                .fuel(FuelType.GASOLINE)
                .transmission(TransmissionType.AUTOMATIC)
                .drive(DriveType.FWD)
                .key(true)
                .damage("Water")
                .title("Cert of title slvg rebuildable")
                .auctionDate(LocalDateTime.of(2025,5,30,18,30))
                .build();

        var lot2 = Lot.builder()
                .auction(AuctionType.IAAI)
                .lotNumber(12345679)
                .type(LotType.OTHER)
                .make("BMW")
                .model("X5")
                .year(2021)
                .vin("1123456789ABC1234")
                .odometer(odometer2)
                .engine("3.0, 350hp")
                .fuel(FuelType.DIESEL)
                .transmission(TransmissionType.MANUAL)
                .drive(DriveType.AWD)
                .key(false)
                .damage("All over")
                .title("Certificate of destruction")
                .auctionDate(LocalDateTime.of(2025,5,31,18,30))
                .build();

        lotRepository.saveAll(List.of(lot1, lot2));
    }

    @ParameterizedTest
    @CsvSource({"12345678", "12345679"})
    void testByLotNumber(Integer lotNumber) {
        Specification<Lot> spec = LotSpecification.lotNumberEquals(lotNumber);
        PageRequest pageRequest = PageRequest.of(0,10);

        Page<Lot> result = lotRepository.findAll(spec, pageRequest);

        assertEquals(1, result.getTotalElements());
        assertEquals(lotNumber, result.getContent().getFirst().getLotNumber());
    }

    @ParameterizedTest
    @CsvSource({"COPART", "IAAI"})
    void testByAuction(AuctionType auction) {
        Specification<Lot> spec = LotSpecification.auctionEquals(auction);
        PageRequest pageRequest = PageRequest.of(0,10);

        Page<Lot> result = lotRepository.findAll(spec, pageRequest);

        assertEquals(1, result.getTotalElements());
        assertEquals(auction, result.getContent().getFirst().getAuction());
    }

    @ParameterizedTest
    @CsvSource({"CAR", "OTHER"})
    void testByType(LotType type) {
        Specification<Lot> spec = LotSpecification.typeEquals(type);
        PageRequest pageRequest = PageRequest.of(0,10);

        Page<Lot> result = lotRepository.findAll(spec, pageRequest);

        assertEquals(1, result.getTotalElements());
        assertEquals(type, result.getContent().getFirst().getType());
    }

    @ParameterizedTest
    @CsvSource({"MERCEDES", "mercedes"})
    void testByMake(String make) {
        Specification<Lot> spec = LotSpecification.hasMake(make);
        PageRequest pageRequest = PageRequest.of(0,10);

        Page<Lot> result = lotRepository.findAll(spec, pageRequest);
        var makeResult = result.getContent().getFirst().getMake();

        assertEquals(1, result.getTotalElements());
        assertTrue(makeResult.toLowerCase().contains(make.toLowerCase()));
    }

    @ParameterizedTest
    @CsvSource({"CLass", "class"})
    void testByModel(String model) {
        Specification<Lot> spec = LotSpecification.hasModel(model);
        PageRequest pageRequest = PageRequest.of(0,10);

        Page<Lot> result = lotRepository.findAll(spec, pageRequest);
        var modelResult = result.getContent().getFirst().getModel();

        assertEquals(1, result.getTotalElements());
        assertTrue(modelResult.toLowerCase().contains(model.toLowerCase()));
    }

    @ParameterizedTest
    @CsvSource({"2022, 2024", "2021, 2023", "2024, 2024"})
    void testByYear(Integer yearFrom, Integer yearTo) {
        Specification<Lot> spec = LotSpecification.yearBetween(yearFrom, yearTo);
        PageRequest pageRequest = PageRequest.of(0,10);

        Page<Lot> result = lotRepository.findAll(spec, pageRequest);
        var yearResult = result.getContent().getFirst().getYear();

        assertEquals(1, result.getTotalElements());
        assertTrue(yearFrom <= yearResult && yearTo >= yearResult);
    }

    @ParameterizedTest
    @CsvSource({"0123456789abc1234", "0123456789ABC1234"})
    void testByVin(String vin) {
        Specification<Lot> spec = LotSpecification.hasVin(vin);
        PageRequest pageRequest = PageRequest.of(0,10);

        Page<Lot> result = lotRepository.findAll(spec, pageRequest);
        var vinResult = result.getContent().getFirst().getVin();

        assertEquals(1, result.getTotalElements());
        assertEquals(vin.toLowerCase(), vinResult.toLowerCase());
    }

    @ParameterizedTest
    @CsvSource({"2.0", "350hp"})
    void testByEngine(String engine) {
        Specification<Lot> spec = LotSpecification.hasEngine(engine);
        PageRequest pageRequest = PageRequest.of(0,10);

        Page<Lot> result = lotRepository.findAll(spec, pageRequest);
        var engineResult = result.getContent().getFirst().getEngine();

        assertEquals(1, result.getTotalElements());
        assertTrue(engineResult.toLowerCase().contains(engine.toLowerCase()));
    }

    @ParameterizedTest
    @CsvSource({"GASOLINE", "DIESEL"})
    void testByFuel(FuelType fuel) {
        Specification<Lot> spec = LotSpecification.fuelEquals(fuel);
        PageRequest pageRequest = PageRequest.of(0,10);

        Page<Lot> result = lotRepository.findAll(spec, pageRequest);

        assertEquals(1, result.getTotalElements());
        assertEquals(fuel, result.getContent().getFirst().getFuel());
    }

    @ParameterizedTest
    @CsvSource({"AUTOMATIC", "MANUAL"})
    void testByTransmission(TransmissionType transmission) {
        Specification<Lot> spec = LotSpecification.transmissionEquals(transmission);
        PageRequest pageRequest = PageRequest.of(0,10);

        Page<Lot> result = lotRepository.findAll(spec, pageRequest);

        assertEquals(1, result.getTotalElements());
        assertEquals(transmission, result.getContent().getFirst().getTransmission());
    }

    @ParameterizedTest
    @CsvSource({"AWD", "FWD"})
    void testByDrive(DriveType drive) {
        Specification<Lot> spec = LotSpecification.driveEquals(drive);
        PageRequest pageRequest = PageRequest.of(0,10);

        Page<Lot> result = lotRepository.findAll(spec, pageRequest);

        assertEquals(1, result.getTotalElements());
        assertEquals(drive, result.getContent().getFirst().getDrive());
    }

    @ParameterizedTest
    @CsvSource({"true", "false"})
    void testByKey(Boolean key) {
        Specification<Lot> spec = LotSpecification.keyEquals(key);
        PageRequest pageRequest = PageRequest.of(0,10);

        Page<Lot> result = lotRepository.findAll(spec, pageRequest);

        assertEquals(1, result.getTotalElements());
        assertEquals(key, result.getContent().getFirst().getKey());
    }

    @ParameterizedTest
    @CsvSource({"all", "ALL"})
    void testByDamage(String damage) {
        Specification<Lot> spec = LotSpecification.hasDamage(damage);
        PageRequest pageRequest = PageRequest.of(0,10);

        Page<Lot> result = lotRepository.findAll(spec, pageRequest);
        var damageResult = result.getContent().getFirst().getDamage();

        assertEquals(1, result.getTotalElements());
        assertTrue(damageResult.toLowerCase().contains(damage.toLowerCase()));
    }

    @ParameterizedTest
    @CsvSource({"destruction", "SLVG"})
    void testByTitle(String title) {
        Specification<Lot> spec = LotSpecification.hasTitle(title);
        PageRequest pageRequest = PageRequest.of(0,10);

        Page<Lot> result = lotRepository.findAll(spec, pageRequest);
        var testResult = result.getContent().getFirst().getTitle();

        assertEquals(1, result.getTotalElements());
        assertTrue(testResult.toLowerCase().contains(title.toLowerCase()));
    }

    @ParameterizedTest
    @CsvSource({
            "2025-04-01, 2025-05-30",
            "2025-05-30, 2025-05-30",
            "2025-05-31, 2025-06-30"
    })
    void testByAuctionDate(LocalDate dateFrom, LocalDate dateTo) {
        Specification<Lot> spec = LotSpecification.auctionDateBetween(dateFrom, dateTo);
        PageRequest pageRequest = PageRequest.of(0,10);

        Page<Lot> result = lotRepository.findAll(spec, pageRequest);
        var auctionDateResult = result.getContent().getFirst().getAuctionDate();

        assertEquals(1, result.getTotalElements());
        assertTrue(auctionDateResult.isAfter(dateFrom.atStartOfDay()));
        assertTrue(auctionDateResult.isBefore(dateTo.plusDays(1).atStartOfDay()));
    }

    @ParameterizedTest
    @CsvSource({"100, 123","100, 124","123, 125"})
    void testByOdometerValue(Integer valueFrom, Integer valueTo) {
        Specification<Lot> spec = LotSpecification.odometerValueBetween(valueFrom, valueTo);
        PageRequest pageRequest = PageRequest.of(0,10);

        Page<Lot> result = lotRepository.findAll(spec, pageRequest);
        var valueResult = result.getContent().getFirst().getOdometer().getValue();

        assertEquals(1, result.getTotalElements());
        assertTrue(valueFrom <= valueResult && valueTo >= valueResult);
    }


    @ParameterizedTest
    @CsvSource({"ACTUAL, NOT_ACTUAL"})
    void testByOdometerStatus(OdometerStatus status) {
        Specification<Lot> spec = LotSpecification.odometerStatusEqual(status);
        PageRequest pageRequest = PageRequest.of(0,10);

        Page<Lot> result = lotRepository.findAll(spec, pageRequest);

        assertEquals(1, result.getTotalElements());
        assertEquals(status, result.getContent().getFirst().getOdometer().getStatus());
    }


    @ParameterizedTest
    @CsvSource({"MILES, KILOMETERS"})
    void testByOdometerUnit(OdometerUnit unit) {
        Specification<Lot> spec = LotSpecification.odometerUnitEquals(unit);
        PageRequest pageRequest = PageRequest.of(0,10);

        Page<Lot> result = lotRepository.findAll(spec, pageRequest);

        assertEquals(1, result.getTotalElements());
        assertEquals(unit, result.getContent().getFirst().getOdometer().getUnit());
    }
}