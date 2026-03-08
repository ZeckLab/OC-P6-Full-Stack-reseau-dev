package com.openclassrooms.mddapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.openclassrooms.mddapi.domain.User;
import com.openclassrooms.mddapi.dto.request.RegisterDTO;
import com.openclassrooms.mddapi.dto.response.UpdatedUserDTO;
import com.openclassrooms.mddapi.dto.response.UserDTO;

/**
 * Mapper responsible for converting between User entities and their DTO representations.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
    /**
     * Converts a User entity into a UserDTO.
     */
    UserDTO toDto(User user);

    /**
     * Converts a RegisterDTO into a User entity.
     * Password is intentionally ignored here for security reasons.
     */
    @Mapping(target = "hashPassword", source = "password", ignore = true) // Password should not be mapped from DTO to entity for security reasons
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "subscriptions", ignore = true)
    User toEntity(RegisterDTO registerDto);

    /**
     * Converts a User entity into an UpdatedUserDTO,
     * optionally including a newly generated JWT token.
     */
    UpdatedUserDTO toUpdatedUserDto(User user, String token);
}
