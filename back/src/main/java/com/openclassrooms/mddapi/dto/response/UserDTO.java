package com.openclassrooms.mddapi.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@JsonPropertyOrder({ "id", "email", "username" })
public class UserDTO {

    private Long id;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String username;
}
