package com.url.shortener.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.url.shortener.model.entity.UrlEntity;
import com.url.shortener.model.repository.UrlRepository;

import java.util.Base64;

@Service
public class UrlService {

    private final UrlRepository urlRepository;

    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @Cacheable(value = "shortenedUrls", key = "#originalUrl")
    public String shortenUrl(String originalUrl) {
        return urlRepository.findByOriginalUrl(originalUrl)
                .map(UrlEntity::getShortUrl)
                .orElseGet(() -> {
                    String shortUrl = generateHash(originalUrl);
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

    private String generateHash(String url) {
        return Base64.getUrlEncoder().encodeToString(url.getBytes()).substring(0, 8);
    }
}

