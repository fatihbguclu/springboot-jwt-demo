package com.jwt.demo.mapper;

import com.jwt.demo.dto.UserDto;
import com.jwt.demo.dto.UserResponseDto;
import com.jwt.demo.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserResponseMapper {

    UserResponseDto toUserResponseDto(User user);
    UserDto toUserDto(UserDto userDto);

}
