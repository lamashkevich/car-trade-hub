package com.lamashkevich.lotservice.controller;

import com.lamashkevich.lotservice.dto.*;
import com.lamashkevich.lotservice.entity.*;
import com.lamashkevich.lotservice.exception.LotAlreadyExistsException;
import com.lamashkevich.lotservice.exception.LotNotFoundException;
import com.lamashkevich.lotservice.service.LotService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LotController.class)
class LotControllerTest {

    private static final String BASE_URL = "/api/v1/lots";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LotService lotService;

    @Test
    void getById_whenLotIsFound() throws Exception {
        var lotId = 1L;
        var response = getLotResponseDto(lotId);
        when(lotService.findById(lotId)).thenReturn(response);

        mockMvc.perform(get(BASE_URL + "/" + lotId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(lotId))
                .andExpect(jsonPath("$.vin").value(response.vin()))
                .andExpect(jsonPath("$.odometer.value").value(response.odometer().value()));
    }

    @Test
    void getById_whenLotIsNotFound() throws Exception {
        var lotId = 1L;
        when(lotService.findById(lotId)).thenThrow(LotNotFoundException.class);

        mockMvc.perform(get(BASE_URL + "/" + lotId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_whenLotDoesNotExist() throws Exception {
        var request = getLotCreateJson();

        var response = getLotResponseDto(1L);
        when(lotService.create(any(LotCreateDto.class))).thenReturn(response);

        mockMvc.perform(post(BASE_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.vin").value(response.vin()))
                .andExpect(jsonPath("$.odometer.value").value(response.odometer().value()));
    }

    @Test
    void create_whenLotAlreadyExists() throws Exception {
        var request = getLotCreateJson();

        when(lotService.create(any(LotCreateDto.class))).thenThrow(LotAlreadyExistsException.class);

        mockMvc.perform(post(BASE_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isConflict());
    }

    @Test
    void create_whenRequestIsNotValid() throws Exception {
        var request = """
                {
                    "year": 1800,
                    "vin": "1234",
                    "odometer": {
                        "value": -1
                    },
                    "engine": "",
                    "damage": "",
                    "title": "",
                    "auctionDate": "20-12-2020 17:00"
                }
                """;

        when(lotService.create(any(LotCreateDto.class))).thenReturn(null);

        mockMvc.perform(post(BASE_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("Lot number is required")))
                .andExpect(content().string(containsString("Auction type is required")))
                .andExpect(content().string(containsString("Lot type is required")))
                .andExpect(content().string(containsString("Year must be at least 1900")))
                .andExpect(content().string(containsString("VIN must be exactly 17 characters")))
                .andExpect(content().string(containsString("Odometer value be at least 0")))
                .andExpect(content().string(containsString("Odometer unit is required")))
                .andExpect(content().string(containsString("Odometer status is required")))
                .andExpect(content().string(containsString("Engine must be between 3 and 20 characters")))
                .andExpect(content().string(containsString("Damage must contain more than 3 characters")))
                .andExpect(content().string(containsString("Title must be between 3 and 100 characters")))
                .andExpect(content().string(containsString("Auction date cannot be in the past")));
    }

    @Test
    void deleteById_whenLotIsFound() throws Exception {
        var lotId = 1L;
        var response = getLotResponseDto(lotId);
        when(lotService.deleteById(lotId)).thenReturn(response);

        mockMvc.perform(delete(BASE_URL + "/" + lotId).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(lotId));
    }

    @Test
    void deleteById_whenLotIsNotFound() throws Exception {
        var lotId = 1L;
        when(lotService.deleteById(lotId)).thenThrow(LotNotFoundException.class);

        mockMvc.perform(delete(BASE_URL + "/" + lotId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAll_whenRequestIsEmpty() throws Exception {
        var response = new PageDto<LotResponseDto>(8, 2, 1, 5,
                "id", Sort.Direction.ASC, List.of());
        when(lotService.findAllByFilter(any(LotFilter.class), any(PaginationDto.class))).thenReturn(response);

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getAll_whenRequestIsInvalid() throws Exception {
        var response = new PageDto<LotResponseDto>(1, 1, 1, 1,
                "id", Sort.Direction.ASC, List.of());

        when(lotService.findAllByFilter(any(LotFilter.class), any(PaginationDto.class))).thenReturn(response);

        mockMvc.perform(get(BASE_URL)
                        .param("page", "-1")
                        .param("size", "-1")
                        .param("sort", "INVALID")
                        .param("direction", "INVALID")
                        .param("auctionType","COPART"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    private LotResponseDto getLotResponseDto(Long id) {
        return new LotResponseDto(
                id,
                12345678,
                AuctionType.COPART,
                LotType.CAR,
                "BMW",
                "X5",
                2020,
                "0123456789ABC1234",
                new OdometerResponseDto(123, OdometerUnit.MILES, OdometerStatus.ACTUAL),
                "3.0, 372HP",
                FuelType.DIESEL,
                TransmissionType.AUTOMATIC,
                DriveType.AWD,
                true,
                "unknown",
                "unknown",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    private String getLotCreateJson() {
        return """
            {
                "lotNumber": 12345678,
                "auction": "COPART",
                "type": "CAR",
                "make": "BMW",
                "model": "X5",
                "year": 2020,
                "vin": "0123456789ABC1234",
                "odometer": {
                    "value": 123,
                    "unit": "KILOMETERS",
                    "status": "ACTUAL"
                },
                "engine": "3.0, 372HP",
                "fuel": "GASOLINE",
                "transmission": "MANUAL",
                "drive": "AWD",
                "key": true,
                "damage": "1234",
                "title": "1234",
                "auctionDate": "%s"
            }
            """.formatted(
                    LocalDateTime.now().plusDays(1)
                            .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
    }
}