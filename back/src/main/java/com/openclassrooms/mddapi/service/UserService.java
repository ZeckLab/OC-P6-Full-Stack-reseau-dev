package com.openclassrooms.mddapi.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.openclassrooms.mddapi.dto.UpdateUserDTO;
import com.openclassrooms.mddapi.exception.BadRequestException;
import com.openclassrooms.mddapi.domain.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.constants.ErrorMessages;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User updateUser(User user, UpdateUserDTO updateUserDTO) {

        // verify if the email is not null and no use before updating
        if(updateUserDTO.getEmail() != null && !updateUserDTO.getEmail().equals(user.getEmail())) {
            if(userRepository.existsByEmail(updateUserDTO.getEmail())) {
                throw new BadRequestException(ErrorMessages.EMAIL_ALREADY_IN_USE + " " +updateUserDTO.getEmail());
            }
            user.setEmail(updateUserDTO.getEmail());
        }

        // verify if the username is not null and no use before updating
        if(updateUserDTO.getUsername() != null && !updateUserDTO.getUsername().equals(user.getUsername())) {
            if(userRepository.existsByUsername(updateUserDTO.getUsername())) {
                throw new BadRequestException(ErrorMessages.USERNAME_ALREADY_IN_USE + " " + updateUserDTO.getUsername());
            }
            user.setUsername(updateUserDTO.getUsername());
        }

        // verify if the password is not null and not empty before updating
        if(updateUserDTO.getPassword() != null && !updateUserDTO.getPassword().isEmpty()) {
            user.setHashPassword(passwordEncoder.encode(updateUserDTO.getPassword()));

        }

        return userRepository.save(user);
    }

}
