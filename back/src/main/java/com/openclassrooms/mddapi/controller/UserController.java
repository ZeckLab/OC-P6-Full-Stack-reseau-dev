package com.openclassrooms.mddapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.mddapi.domain.User;
import com.openclassrooms.mddapi.dto.request.UpdateUserDTO;
import com.openclassrooms.mddapi.dto.response.UpdatedUserDTO;
import com.openclassrooms.mddapi.dto.response.UserDTO;
import com.openclassrooms.mddapi.mapper.UserMapper;
import com.openclassrooms.mddapi.service.JwtService;
import com.openclassrooms.mddapi.service.UserService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserMapper userMapper;
    private final UserService userService;
    private final JwtService jwtService;

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser() {
        log.info("GET /users/me - Fetching current user");

        User user = userService.getCurrentUser();
        log.info("GET /users/me - User data successfully retrieved");

        return ResponseEntity.ok().body(userMapper.toDto(user));
    }

    @PatchMapping("/me")
    public ResponseEntity<UpdatedUserDTO> updateCurrentUser(@Valid @RequestBody UpdateUserDTO updateUserDTO) {
        log.info("PATCH /users/me - Update request received");
        String newToken = null;

        String oldUsername = userService.getCurrentUser().getUsername();
        log.info("PATCH /users/me - Current username: {}", oldUsername);

        User updatedUser = userService.updateUser(updateUserDTO);
        log.info("PATCH /users/me - User {} updated successfully", updatedUser.getUsername());

        if(!oldUsername.equals(updatedUser.getUsername())) {
            log.info("PATCH /users/me - Username changed from '{}' to '{}', generating new token", oldUsername, updatedUser.getUsername());
            newToken = jwtService.generateToken(updatedUser.getUsername());
        } else {
            log.info("PATCH /users/me - Username not changed, no new token generated");
        }

        return ResponseEntity.ok().body(userMapper.toUpdatedUserDto(updatedUser, newToken));
    }
}
