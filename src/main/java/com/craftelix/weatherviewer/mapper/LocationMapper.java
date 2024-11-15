package com.craftelix.weatherviewer.mapper;

import com.craftelix.weatherviewer.dto.location.LocationRequestDto;
import com.craftelix.weatherviewer.dto.location.LocationWeatherDto;
import com.craftelix.weatherviewer.entity.Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LocationMapper {
    LocationMapper INSTANCE = Mappers.getMapper(LocationMapper.class);

    @Mapping(target = "id", ignore = true)
    Location toEntity(LocationRequestDto locationRequestDto);

    @Mapping(target = "weatherApi", ignore = true)
    List<LocationWeatherDto> toDto(List<Location> locations);
}
