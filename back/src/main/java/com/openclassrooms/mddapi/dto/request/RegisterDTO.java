package com.openclassrooms.mddapi.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import com.openclassrooms.mddapi.constants.ErrorMessages;
import com.openclassrooms.mddapi.constants.AppMessages;

@Data
public class RegisterDTO{
    @NotBlank(message = ErrorMessages.EMAIL_REQUIRED)
    @Email(message = ErrorMessages.EMAIL_INVALID)
    private String email;

    @NotBlank(message = ErrorMessages.USERNAME_REQUIRED)
    @Size(min = 3, max = 30, message = ErrorMessages.USERNAME_SIZE_INVALID)
    @Pattern(regexp = AppMessages.USERNAME_REGEX, message = ErrorMessages.USERNAME_CANNOT_CONTAIN_AT)
    private String username;

    @NotBlank(message = ErrorMessages.PASSWORD_REQUIRED)
    @Pattern(
                regexp = AppMessages.PASSWORD_REGEX,
                message = ErrorMessages.PASSWORD_INVALID
        )
    private String password;
}