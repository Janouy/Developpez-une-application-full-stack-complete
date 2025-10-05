package com.openclassrooms.mddapi.dto;

import java.time.Instant;

/**
 * Représentation d'un post.
 * @param id          identifiant du post
 * @param subjectName nom du sujet
 * @param authorName  nom de l'auteur
 * @param title       titre du post
 * @param content     contenu du post
 * @param createdAt   date de création
 */
public record PostResponse(
        Long id,
        String subjectName,
        String authorName,
        String title,
        String content,
        Instant createdAt
) {}
