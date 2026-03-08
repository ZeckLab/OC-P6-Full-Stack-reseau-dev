package com.openclassrooms.mddapi.dto.request;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import com.openclassrooms.mddapi.constants.ErrorMessages;

/**
 * DTO containing user credentials for authentication.
 */
@Data
public class LoginDTO {
    @NotBlank(message = ErrorMessages.EMAIL_USERNAME_REQUIRED)
    private String username;

    @NotBlank(message = ErrorMessages.PASSWORD_REQUIRED)
    private String password;
}

