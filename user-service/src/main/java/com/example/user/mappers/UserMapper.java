package com.example.user.mappers;

import com.example.user.dto.UserDto;
import com.example.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    @Mapping(target = ".", source = "dto")
    User map(UserDto dto);

    @Mapping(target = ".", source = "user")
    UserDto map(User user);
}
