package com.lamashkevich.lotservice.dto;

import com.lamashkevich.lotservice.entity.Lot;
import lombok.Getter;
import org.springframework.data.domain.Sort;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public final class PaginationDto {

    public static final Integer DEFAULT_PAGE = 1;
    public static final Integer DEFAULT_SIZE = 10;
    public static final String DEFAULT_SORT_FIELD = "auctionDate";
    public static final Sort.Direction DEFAULT_SORT_DIRECTION = Sort.Direction.DESC;
    public static final Integer MAX_PAGE_SIZE = 50;

    private static final Set<String> ALLOWED_SORT_FIELDS = Stream.of(Lot.class.getDeclaredFields())
            .map(Field::getName)
            .collect(Collectors.toSet());

    private final Integer page;
    private final Integer size;
    private final String sortBy;
    private final Sort.Direction sortDirection;

    public PaginationDto(Integer page, Integer size, String sortBy, String sortDirection) {
        this.page = (page == null || page < 1) ? DEFAULT_PAGE : page;
        this.size = (size == null || size < 1) ? DEFAULT_SIZE : Math.min(size, MAX_PAGE_SIZE);
        this.sortBy = (sortBy == null || !ALLOWED_SORT_FIELDS.contains(sortBy)) ? DEFAULT_SORT_FIELD : sortBy;
        this.sortDirection = convertToSortDirection(sortDirection);
    }

    private static Sort.Direction convertToSortDirection(String direction) {
        if (direction == null || direction.isBlank()) {
            return DEFAULT_SORT_DIRECTION;
        }

        try {
            return Sort.Direction.fromString(direction.toUpperCase());
        } catch (IllegalArgumentException e) {
            return DEFAULT_SORT_DIRECTION;
        }
    }

}