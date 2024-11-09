package com.craftelix.weatherviewer.mapper;

import com.craftelix.weatherviewer.dto.user.UserDto;
import com.craftelix.weatherviewer.dto.user.UserSignupDto;
import com.craftelix.weatherviewer.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", expression = "java(userSignupDto.getUsername().toLowerCase())")
    User toEntity(UserSignupDto userSignupDto);

    UserDto toDto(User user);
}
