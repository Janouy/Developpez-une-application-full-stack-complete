package com.openclassrooms.mddapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Payload de connexion.
 * @param login identifiant de connexion (email ou nom d'utilisateur)
 * @param password mot de passe
 */
public record LoginRequest(@NotBlank(message = "Login is required")
                           String login,

                           @NotBlank(message = "Password is required")
                           String password)  {}
