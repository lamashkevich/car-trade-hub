package com.lamashkevich.lotservice.service;

import com.lamashkevich.lotservice.dto.LotCreateDto;
import com.lamashkevich.lotservice.dto.LotResponseDto;
import com.lamashkevich.lotservice.entity.Lot;
import com.lamashkevich.lotservice.exception.LotAlreadyExistsException;
import com.lamashkevich.lotservice.exception.LotNotFoundException;
import com.lamashkevich.lotservice.mapper.LotMapper;
import com.lamashkevich.lotservice.repository.LotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LotService {

    private final LotRepository lotRepository;
    private final LotMapper lotMapper;

    public LotResponseDto findById(Long id) {
        log.info("Getting lot by id: {}", id);
        return lotRepository.findById(id)
                .map(lotMapper::lotToLotResponseDto)
                .orElseThrow(() -> new LotNotFoundException(id));
    }

    @Transactional
    public LotResponseDto create(LotCreateDto lotCreateDto) {
        log.info("Creating lot: {}", lotCreateDto);

        if (lotRepository.existsByLotNumber(lotCreateDto.lotNumber())) {
            throw new LotAlreadyExistsException(lotCreateDto.lotNumber());
        }

        return Optional.of(lotCreateDto)
                .map(lotMapper::lotCreateDtoToLot)
                .map(lotRepository::save)
                .map(lotMapper::lotToLotResponseDto)
                .orElseThrow(() -> new RuntimeException("Failed to create lot"));
    }

    @Transactional
    public LotResponseDto deleteById(Long id) {
        log.info("Deleting lot: {}", id);
        Lot lot = lotRepository.findById(id)
                .orElseThrow(() -> new LotNotFoundException(id));

        lotRepository.delete(lot);

        return lotMapper.lotToLotResponseDto(lot);
    }
}
