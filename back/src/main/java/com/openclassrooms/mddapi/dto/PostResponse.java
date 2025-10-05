package com.openclassrooms.mddapi.dto;

import java.time.Instant;

public record PostResponse(
        Long id,
        String subjectName,
        String authorName,
        String title,
        String content,
        Instant createdAt
) {}
