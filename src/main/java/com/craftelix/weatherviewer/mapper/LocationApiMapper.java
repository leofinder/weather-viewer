package com.craftelix.weatherviewer.mapper;

import com.craftelix.weatherviewer.dto.api.LocationApiDto;
import com.craftelix.weatherviewer.entity.Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LocationApiMapper {
    LocationApiMapper INSTANCE = Mappers.getMapper(LocationApiMapper.class);

    @Mapping(target = "id", ignore = true)
    Location toEntity(LocationApiDto locationApiDto);

}
