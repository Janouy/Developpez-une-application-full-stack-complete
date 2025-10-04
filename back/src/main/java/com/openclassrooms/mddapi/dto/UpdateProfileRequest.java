package com.openclassrooms.mddapi.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(
        @NotBlank(message = "Name is required")
        @Size(min = 4, max = 30, message = "Name must be 4 to 30 chars")
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @JsonAlias({"password", "new_password"})
        @Size(min = 8, max = 64, message = "New password must be 8..64 chars")
        @Pattern.List({
                @Pattern(regexp = ".*[0-9].*", message = "New password must contain a digit"),
                @Pattern(regexp = ".*[a-z].*", message = "New password must contain a lowercase letter"),
                @Pattern(regexp = ".*[A-Z].*", message = "New password must contain an uppercase letter"),
                @Pattern(regexp = ".*[^A-Za-z0-9].*", message = "New password must contain a special character")
        })
        String newPassword
) {}
