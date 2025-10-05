package com.openclassrooms.mddapi.dto;

import java.time.Instant;

public record CommentResponse(
        Long id,
        Long postId,
        Long authorId,
        String authorName,
        String content,
        Instant createdAt
) {}
