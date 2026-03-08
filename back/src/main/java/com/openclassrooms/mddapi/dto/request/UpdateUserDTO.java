package com.openclassrooms.mddapi.dto.request;

import com.openclassrooms.mddapi.constants.AppMessages;
import com.openclassrooms.mddapi.constants.ErrorMessages;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO used for updating the authenticated user's account information.
 */
@Data
public class UpdateUserDTO {
    @Email(message = ErrorMessages.EMAIL_INVALID)
    private String email;

    @Size(min = 3, max = 30, message = ErrorMessages.USERNAME_SIZE_INVALID)
    @Pattern(regexp = AppMessages.USERNAME_REGEX, message = ErrorMessages.USERNAME_CANNOT_CONTAIN_AT)
    private String username;

    @Pattern(
                regexp = AppMessages.PASSWORD_REGEX,
                message = ErrorMessages.PASSWORD_INVALID
        )
    private String password;
}
