package com.short_url.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.short_url.entity.Url;

public interface UrlRepository extends JpaRepository<Url, UUID> {
    Optional<Url> findByShortCode(String shortCode);
    boolean existsByOriginalUrl(String originalUrl);
    boolean existsByShortCode(String shortCode);

    @Modifying
    @Transactional
    @Query("UPDATE Url u SET u.acessCount = u.acessCount + 1 WHERE u.shortCode = :shortCode")
    void incrementAccessCount(@Param("shortCode") String shortCode);
}
