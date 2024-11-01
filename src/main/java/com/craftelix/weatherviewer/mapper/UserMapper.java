package com.craftelix.weatherviewer.mapper;

import com.craftelix.weatherviewer.dto.UserDto;
import com.craftelix.weatherviewer.dto.UserSignupDto;
import com.craftelix.weatherviewer.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", ignore = true)
    User toEntity(UserSignupDto userSignupDto);

    UserDto toDto(User user);
}
