package com.lamashkevich.lotservice.dto;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.data.domain.Sort;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class PaginationDtoTest {

    @ParameterizedTest
    @MethodSource("argumentsProvider")
    public void defaultValue(Integer page, Integer pageExpected,
                             Integer size, Integer sizeExpected,
                             String sortBy, String sortByExpected,
                             String sortDirection,
                             Sort.Direction sortDirectionExpected) {

        var pagination = new PaginationDto(page, size, sortBy, sortDirection);

        assertEquals(pageExpected, pagination.getPage());
        assertEquals(sizeExpected, pagination.getSize());
        assertEquals(sortByExpected, pagination.getSortBy());
        assertEquals(sortDirectionExpected, pagination.getSortDirection());
    }

    static Stream<Arguments> argumentsProvider() {
        return Stream.of(
                arguments(
                        1, 1,
                        5, 5,
                        "id", "id",
                        "ASC", Sort.Direction.ASC
                ),
                arguments(
                        null, PaginationDto.DEFAULT_PAGE,
                        null, PaginationDto.DEFAULT_SIZE,
                        null, PaginationDto.DEFAULT_SORT_FIELD,
                        null, PaginationDto.DEFAULT_SORT_DIRECTION
                ),
                arguments(
                        0, PaginationDto.DEFAULT_PAGE,
                        0, PaginationDto.DEFAULT_SIZE,
                        "INVALID", PaginationDto.DEFAULT_SORT_FIELD,
                        "INVALID", PaginationDto.DEFAULT_SORT_DIRECTION
                ),
                arguments(
                        null, PaginationDto.DEFAULT_PAGE,
                        999, PaginationDto.MAX_PAGE_SIZE,
                        null, PaginationDto.DEFAULT_SORT_FIELD,
                        null, PaginationDto.DEFAULT_SORT_DIRECTION
                )
        );
    }
}