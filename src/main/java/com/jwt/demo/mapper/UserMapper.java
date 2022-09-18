package com.jwt.demo.mapper;

import com.jwt.demo.dto.UserDto;
import com.jwt.demo.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toUserDto(User user);
    User toUser(UserDto userDto);

}
