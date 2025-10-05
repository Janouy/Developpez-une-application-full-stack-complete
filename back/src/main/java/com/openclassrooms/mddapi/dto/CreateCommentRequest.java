package com.openclassrooms.mddapi.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Payload de création d'un commentaire.
 * @param content contenu du commentaire (requis)
 */
public record CreateCommentRequest(
        @NotBlank(message = "Content is required")
        String content
) {}
