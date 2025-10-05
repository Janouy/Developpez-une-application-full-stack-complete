package com.openclassrooms.mddapi.dto;

/**
 * Profil utilisateur (lecture seule).
 * @param name  nom d'affichage
 * @param email email de l'utilisateur
 */
public record UserResponse (String name, String email){
}
