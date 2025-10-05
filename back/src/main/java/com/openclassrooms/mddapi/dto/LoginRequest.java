package com.openclassrooms.mddapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Payload de connexion.
 * @param email    email de connexion (format valide)
 * @param password mot de passe
 */
public record LoginRequest(@NotBlank(message = "Email is required")
                           @Email(message = "Invalid email format")
                           String email,

                           @NotBlank(message = "Password is required")
                           String password)  {}
