package com.url.shortener.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class RedisITest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private UrlService urlService;

    @Test
    void testRedisCaching() {
        // Arrange
        String originalUrl = "https://example1.com";

        // Act
        String shortUrl = urlService.shortenUrl(originalUrl);

        // Assert
        String cachedShortUrl = redisTemplate.opsForValue().get("shortenedUrls::" + originalUrl);
        assertNotNull(cachedShortUrl, "Shortened URL should be cached in Redis");
        assertEquals(shortUrl, cachedShortUrl, "Cached value should match the shortened URL");

        // Act
        String resolvedOriginalUrl = urlService.resolveUrl(shortUrl);
        assertEquals(originalUrl, resolvedOriginalUrl, "Resolved URL should match the original URL");

        // Assert
        String cachedOriginalUrl = redisTemplate.opsForValue().get("originalUrls::" + shortUrl);
        assertNotNull(cachedOriginalUrl, "Original URL should be cached in Redis");
        assertEquals(originalUrl, cachedOriginalUrl, "Cached value should match the original URL");
    }
}
