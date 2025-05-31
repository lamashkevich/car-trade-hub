package com.lamashkevich.lotservice.mapper;

import com.lamashkevich.lotservice.dto.LotCreateDto;
import com.lamashkevich.lotservice.dto.LotResponseDto;
import com.lamashkevich.lotservice.entity.Lot;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LotMapper {

    LotResponseDto lotToLotResponseDto(Lot lot);

    Lot lotCreateDtoToLot(LotCreateDto lotCreateDto);

}
