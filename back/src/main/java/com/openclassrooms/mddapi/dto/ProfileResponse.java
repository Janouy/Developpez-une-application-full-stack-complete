package com.openclassrooms.mddapi.dto;

/**
 * Profil utilisateur (réponse).
 * @param name  nom d'affichage
 * @param email email de connexion
 * @param token nouveau JWT si credentials modifiés, sinon {@code null}
 */
public record ProfileResponse(String name, String email, String token) {}
