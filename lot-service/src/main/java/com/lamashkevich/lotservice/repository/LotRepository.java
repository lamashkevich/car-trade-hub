package com.lamashkevich.lotservice.repository;

import com.lamashkevich.lotservice.entity.Lot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LotRepository extends JpaRepository<Lot, Long> {
    boolean existsByLotNumber(Integer lotNumber);
}
