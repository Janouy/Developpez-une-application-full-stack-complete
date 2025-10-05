package com.openclassrooms.mddapi.dto;

import java.time.Instant;

/**
 * Commentaire d'un post.
 * @param id        identifiant du commentaire
 * @param postId    identifiant du post concerné
 * @param authorId  identifiant de l'auteur
 * @param authorName nom d'affichage de l'auteur
 * @param content   contenu du commentaire
 * @param createdAt date de création (UTC)
 */
public record CommentResponse(
        Long id,
        Long postId,
        Long authorId,
        String authorName,
        String content,
        Instant createdAt
) {}
