package com.short_url.dto;

import java.time.LocalDateTime;

public record UrlResponseDto(
    String originalUrl,
    String shortCode,
    Long accessCount,
    LocalDateTime createdAt
) {}
