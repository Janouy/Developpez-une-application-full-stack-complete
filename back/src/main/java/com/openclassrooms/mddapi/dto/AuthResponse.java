package com.openclassrooms.mddapi.dto;

/**
 * Réponse d'authentification.
 * @param token JWT à envoyer dans l'en-tête {@code Authorization: Bearer <token>}
 */
public record AuthResponse(String token) {}
