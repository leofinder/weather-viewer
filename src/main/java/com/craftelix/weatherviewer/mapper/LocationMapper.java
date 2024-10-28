package com.craftelix.weatherviewer.mapper;

import com.craftelix.weatherviewer.dto.LocationRequestDto;
import com.craftelix.weatherviewer.dto.api.LocationDto;
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

    @Mapping(target = "id", ignore = true)
    Location toEntity(LocationDto locationDto);

    LocationDto toDto(Location location);

    List<LocationDto> toDto(List<Location> locations);
}
