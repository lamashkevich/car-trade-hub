package com.lamashkevich.lotservice.dto;

import org.springframework.data.domain.Sort;

import java.util.List;

public record PageDto<T>(
        long totalElements,
        int totalPages,
        int pageNumber,
        int pageSize,
        String sortBy,
        Sort.Direction sortDirection,
        List<T> content
) {
}
