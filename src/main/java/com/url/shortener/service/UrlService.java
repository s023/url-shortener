package com.url.shortener.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.url.shortener.model.entity.UrlEntity;
import com.url.shortener.model.repository.UrlRepository;
import com.url.shortener.util.SnowflakeIdGenerator;

@Service
public class UrlService {
    private final SnowflakeIdGenerator idGenerator;

    private final UrlRepository urlRepository;

    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
        this.idGenerator = new SnowflakeIdGenerator(1L, 1L);
    }

    @Cacheable(value = "shortenedUrls", key = "#originalUrl")
    public String shortenUrl(String originalUrl) {
        return urlRepository.findByOriginalUrl(originalUrl)
                .map(UrlEntity::getShortUrl)
                .orElseGet(() -> {
                    long id = idGenerator.nextId();
                    String shortUrl = Long.toString(id, 36);
                    urlRepository.save(UrlEntity.builder()
                            .originalUrl(originalUrl)
                            .shortUrl(shortUrl)
                            .build());
                    return shortUrl;
                });
    }

    @Cacheable(value = "originalUrls", key = "#shortUrl")
    public String resolveUrl(String shortUrl) {
        return urlRepository.findByShortUrl(shortUrl)
                .map(UrlEntity::getOriginalUrl)
                .orElseThrow(() -> new RuntimeException("URL not found!"));
    }
}

