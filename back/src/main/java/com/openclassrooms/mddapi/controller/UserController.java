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
import com.openclassrooms.mddapi.dto.UpdateUserDTO;
import com.openclassrooms.mddapi.dto.UserDTO;
import com.openclassrooms.mddapi.mapper.UserMapper;
import com.openclassrooms.mddapi.service.UserService;

import jakarta.validation.Valid;

import static com.openclassrooms.mddapi.constants.ErrorMessages.USER_NOT_FOUND;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserService userService;

    public UserController(UserRepository userRepository, UserMapper userMapper, UserService userService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        log.info("GET /users/me - Authenticated user: {}", username);

        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND + username));
        log.info("GET /users/me - User data successfully retrieved for {}", username);

        UserDTO userDTO = userMapper.toDto(user);

        return ResponseEntity.ok().body(userDTO);
    }

    @PatchMapping("/me")
    public ResponseEntity<UserDTO> updateCurrentUser(Authentication authentication, @Valid @RequestBody UpdateUserDTO updateUserDTO) {
        String username = authentication.getName();
        log.info("PATCH /users/me - Update request received for {}", username);

        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND + username));

        User updatedUser = userService.updateUser(user, updateUserDTO);
        log.info("PATCH /users/me - User {} updated successfully", updatedUser.getUsername());

        return ResponseEntity.ok().body(userMapper.toDto(updatedUser));
    }
}
