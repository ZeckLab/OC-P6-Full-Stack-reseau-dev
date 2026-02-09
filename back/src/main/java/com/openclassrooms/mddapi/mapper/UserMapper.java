package com.openclassrooms.mddapi.mapper;

import org.mapstruct.Mapper;

import com.openclassrooms.mddapi.domain.User;
import com.openclassrooms.mddapi.dto.UserDTO;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDto(User user);
}
