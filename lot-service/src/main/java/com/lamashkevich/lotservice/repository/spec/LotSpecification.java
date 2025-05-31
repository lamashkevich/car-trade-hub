package com.lamashkevich.lotservice.repository.spec;

import com.lamashkevich.lotservice.dto.LotFilter;
import com.lamashkevich.lotservice.entity.*;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class LotSpecification {

    public static Specification<Lot> filter(LotFilter filter) {
        return Specification.where(lotNumberEquals(filter.lotNumber()))
                .and(auctionEquals(filter.auction()))
                .and(typeEquals(filter.type()))
                .and(hasMake(filter.make()))
                .and(hasModel(filter.model()))
                .and(yearBetween(filter.yearFrom(), filter.yearTo()))
                .and(hasVin(filter.vin()))
                .and(hasEngine(filter.engine()))
                .and(fuelEquals(filter.fuel()))
                .and(transmissionEquals(filter.transmission()))
                .and(driveEquals(filter.drive()))
                .and(keyEquals(filter.key()))
                .and(hasDamage(filter.damage()))
                .and(hasTitle(filter.title()))
                .and(auctionDateBetween(filter.auctionDateFrom(), filter.auctionDateTo()))
                .and(odometerValueBetween(filter.odometerValueFrom(), filter.odometerValueTo()))
                .and(odometerUnitEquals(filter.odometerUnit()))
                .and(odometerStatusEqual(filter.odometerStatus()));
    }

    public static Specification<Lot> lotNumberEquals(Integer lotNumber) {
        return (root, query, cb) -> {
            if (lotNumber == null) return cb.conjunction();
            return cb.equal(root.get("lotNumber"), lotNumber);
        };
    }

    public static Specification<Lot> auctionEquals(AuctionType auction) {
        return (root, query, cb) -> {
            if (auction == null)  return cb.conjunction();
            return cb.equal(root.get("auction"), auction);
        };
    }

    public static Specification<Lot> typeEquals(LotType type) {
        return (root, query, cb) -> {
            if (type == null) return cb.conjunction();
            return cb.equal(root.get("type"), type);
        };
    }

    public static Specification<Lot> hasMake(String make) {
        return (root, query, cb) -> {
            if (make == null || make.isEmpty()) return cb.conjunction();
            return cb.like(cb.lower(root.get("make")), "%" + make.toLowerCase() + "%");
        };
    }

    public static Specification<Lot> hasModel(String model) {
        return (root, query, cb) -> {
            if (model == null || model.isEmpty()) return cb.conjunction();
            return cb.like(cb.lower(root.get("model")), "%" + model.toLowerCase() + "%");
        };
    }

    public static Specification<Lot> yearBetween(Integer from, Integer to) {
        return (root, query, cb) -> {
            if (from == null && to == null) return cb.conjunction();
            if (from == null) return cb.lessThanOrEqualTo(root.get("year"), to);
            if (to == null) return cb.greaterThanOrEqualTo(root.get("year"), from);
            return cb.between(root.get("year"), from, to);
        };
    }

    public static Specification<Lot> hasVin(String vin) {
        return (root, query, cb) -> {
            if (vin == null || vin.isEmpty()) return cb.conjunction();
            return cb.like(cb.lower(root.get("vin")), vin.toLowerCase());
        };
    }

    public static Specification<Lot> hasEngine(String engine) {
        return (root, query, cb) -> {
            if (engine == null || engine.isEmpty()) return cb.conjunction();
            return cb.like(cb.lower(root.get("engine")), "%" + engine.toLowerCase() + "%");
        };
    }

    public static Specification<Lot> fuelEquals(FuelType fuel) {
        return (root, query, cb) -> {
            if (fuel == null) return cb.conjunction();
            return cb.equal(root.get("fuel"), fuel);
        };
    }

    public static Specification<Lot> transmissionEquals(TransmissionType transmission) {
        return (root, query, cb) -> {
            if (transmission == null) return cb.conjunction();
            return cb.equal(root.get("transmission"), transmission);
        };
    }

    public static Specification<Lot> driveEquals(DriveType drive) {
        return (root, query, cb) -> {
            if (drive == null) return cb.conjunction();
            return cb.equal(root.get("drive"), drive);
        };
    }

    public static Specification<Lot> keyEquals(Boolean key) {
        return (root, query, cb) -> {
            if (key == null) return cb.conjunction();
            return cb.equal(root.get("key"), key);
        };
    }

    public static Specification<Lot> hasDamage(String damage) {
        return (root, query, cb) -> {
            if (damage == null || damage.isEmpty()) return cb.conjunction();
            return cb.like(cb.lower(root.get("damage")), "%" + damage.toLowerCase() + "%");
        };
    }

    public static Specification<Lot> hasTitle(String title) {
        return (root, query, cb) -> {
            if (title == null || title.isEmpty()) return cb.conjunction();
            return cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
        };
    }

    public static Specification<Lot> auctionDateBetween(LocalDate from, LocalDate to) {
        return (root, query, cb) -> {
            if (from == null && to == null) return cb.conjunction();
            if (from == null) return cb.lessThanOrEqualTo(root.get("auctionDate"), to);
            if (to == null) return cb.greaterThanOrEqualTo(root.get("auctionDate"), from);
            return cb.between(root.get("auctionDate"), from, to.plusDays(1));
        };
    }

    public static Specification<Lot> odometerValueBetween(Integer from, Integer to) {
        return (root, query, cb) -> {
            if (from == null && to == null) return cb.conjunction();
            Join<Lot, Odometer> join = root.join("odometer");
            if (from == null) return cb.lessThanOrEqualTo(join.get("value"), to);
            if (to == null) return cb.greaterThanOrEqualTo(join.get("value"), from);
            return cb.between(join.get("value"), from, to);
        };
    }

    public static Specification<Lot> odometerUnitEquals(OdometerUnit unit) {
        return (root, query, cb) -> {
            if (unit == null) return cb.conjunction();
            Join<Lot, Odometer> join = root.join("odometer");
            return cb.equal(join.get("unit"), unit);
        };
    }

    public static Specification<Lot> odometerStatusEqual(OdometerStatus status) {
        return (root, query, cb) -> {
            if (status == null) return cb.conjunction();
            Join<Lot, Odometer> join = root.join("odometer");
            return cb.equal(join.get("status"), status);
        };
    }
}
