package com.url.shortener.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.url.shortener.model.entity.UrlEntity;

public interface UrlRepository extends JpaRepository<UrlEntity, Long> {
    Optional<UrlEntity> findByShortUrl(String shortUrl);
    Optional<UrlEntity> findByOriginalUrl(String originalUrl);
}
