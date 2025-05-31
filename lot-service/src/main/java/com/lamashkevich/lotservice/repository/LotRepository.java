package com.lamashkevich.lotservice.repository;

import com.lamashkevich.lotservice.entity.Lot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LotRepository extends JpaRepository<Lot, Long>, JpaSpecificationExecutor<Lot> {
    boolean existsByLotNumber(Integer lotNumber);
}
