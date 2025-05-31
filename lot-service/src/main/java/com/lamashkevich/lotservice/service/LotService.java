package com.lamashkevich.lotservice.service;

import com.lamashkevich.lotservice.dto.*;
import com.lamashkevich.lotservice.entity.Lot;
import com.lamashkevich.lotservice.exception.LotAlreadyExistsException;
import com.lamashkevich.lotservice.exception.LotNotFoundException;
import com.lamashkevich.lotservice.mapper.LotMapper;
import com.lamashkevich.lotservice.repository.LotRepository;
import com.lamashkevich.lotservice.repository.spec.LotSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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

    public PageDto<LotResponseDto> findAllByFilter(LotFilter filter, PaginationDto paginationDto) {
        log.info("Getting lot with pagination: {}, filter: {}", paginationDto, filter);

        Specification<Lot> spec = LotSpecification.filter(filter);
        Pageable pageable = PageRequest.of(
                paginationDto.getPage() - 1,
                paginationDto.getSize(),
                Sort.by(paginationDto.getSortDirection(), paginationDto.getSortBy())
        );

        Page<Lot> pageResult = lotRepository.findAll(spec, pageable);

        return new PageDto<>(
                pageResult.getTotalElements(),
                pageResult.getTotalPages(),
                pageResult.getNumber() + 1,
                pageResult.getSize(),
                paginationDto.getSortBy(),
                paginationDto.getSortDirection(),
                pageResult.getContent()
                        .stream()
                        .map(lotMapper::lotToLotResponseDto)
                        .toList()
        );
    }
}
