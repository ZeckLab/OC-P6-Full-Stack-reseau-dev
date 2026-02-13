package com.openclassrooms.mddapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.domain.User;
import com.openclassrooms.mddapi.dto.request.UpdateUserDTO;
import com.openclassrooms.mddapi.dto.response.UserDTO;
import com.openclassrooms.mddapi.mapper.UserMapper;
import com.openclassrooms.mddapi.service.UserService;

import jakarta.validation.Valid;

import static com.openclassrooms.mddapi.constants.ErrorMessages.USER_NOT_FOUND;

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

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        log.info("GET /users/me - Authenticated user: {}", username);

        User user = userService.getCurrentUser(username);
        log.info("GET /users/me - User data successfully retrieved for {}", username);

        return ResponseEntity.ok().body(userMapper.toDto(user));
    }

    @PatchMapping("/me")
    public ResponseEntity<UserDTO> updateCurrentUser(Authentication authentication, @Valid @RequestBody UpdateUserDTO updateUserDTO) {
        String username = authentication.getName();
        log.info("PATCH /users/me - Update request received for {}", username);

        User updatedUser = userService.updateUser(username, updateUserDTO);
        log.info("PATCH /users/me - User {} updated successfully", updatedUser.getUsername());

        return ResponseEntity.ok().body(userMapper.toDto(updatedUser));
    }
}
