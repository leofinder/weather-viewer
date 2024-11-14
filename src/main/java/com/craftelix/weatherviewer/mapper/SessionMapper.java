package com.craftelix.weatherviewer.mapper;

import com.craftelix.weatherviewer.dto.session.SessionDto;
import com.craftelix.weatherviewer.entity.Session;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SessionMapper {
    SessionMapper INSTANCE = Mappers.getMapper(SessionMapper.class);

    SessionDto toDto(Session session);
}
