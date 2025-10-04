// src/main/java/com/openclassrooms/mddapi/dto/UpdateProfileRequest.java
package com.openclassrooms.mddapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(
        @NotBlank(message = "Name is required")
        @Size(min = 4, max = 30, message = "Name must be 4 to 30 chars")
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @Size(min = 8, message = "New password must be at least 8 chars")
        String newPassword
) {}
