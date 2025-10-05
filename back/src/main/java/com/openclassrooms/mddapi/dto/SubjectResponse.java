package com.openclassrooms.mddapi.dto;

/**
 * Sujet avec état d'abonnement.
 * @param id          identifiant du sujet
 * @param name        nom du sujet
 * @param description description du sujet
 * @param subscribed  {@code true} si l'utilisateur courant est abonné
 */
public record SubjectResponse(Long id, String name, String description, boolean subscribed) {}