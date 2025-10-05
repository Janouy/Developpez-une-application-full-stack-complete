package com.openclassrooms.mddapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Positive;

/**
 * Payload de création d'un post.
 * @param subjectId identifiant du sujet (> 0)
 * @param title     titre du post (≤ 50)
 * @param content   contenu du post
 */
public record CreatePostRequest(
        @NotNull(message = "subjectId is required")
        @Positive(message = "subjectId must be > 0")
        Long subjectId,

        @NotBlank(message = "Title is required")
        @Size(max = 50, message = "Title must be <= 50 chars")
        String title,

        @NotBlank(message = "Content is required")
        String content
) {}
