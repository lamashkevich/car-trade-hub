package com.lamashkevich.lotservice.controller;

import com.lamashkevich.lotservice.dto.LotCreateDto;
import com.lamashkevich.lotservice.dto.LotResponseDto;
import com.lamashkevich.lotservice.service.LotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/lots")
public class LotController {

    private final LotService lotService;

    @GetMapping("/{id}")
    public LotResponseDto getById(@PathVariable Long id) {
        return lotService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LotResponseDto create(@Valid @RequestBody LotCreateDto lotCreateDto) {
        return lotService.create(lotCreateDto);
    }

    @DeleteMapping("/{id}")
    public LotResponseDto deleteById(@PathVariable Long id) {
        return lotService.deleteById(id);
    }
}
