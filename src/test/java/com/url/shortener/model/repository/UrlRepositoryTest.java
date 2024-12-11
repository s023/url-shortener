package com.url.shortener.model.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.url.shortener.model.entity.UrlEntity;

@DataJpaTest
class UrlRepositoryTest {

    @Autowired
    private UrlRepository urlRepository;

    @Test
    void testSaveAndFindByOriginalUrl() {
        // Arrange
        UrlEntity entity = new UrlEntity(null, "https://example.com", "abc123");
        urlRepository.save(entity);

        // Act
        Optional<UrlEntity> found = urlRepository.findByOriginalUrl("https://example.com");

        // Assert
        assertTrue(found.isPresent());
        assertEquals("abc123", found.get().getShortUrl());
    }

    @Test
    void testFindByShortUrl() {
        // Arrange
        UrlEntity entity = new UrlEntity(null, "https://example.com", "abc123");
        urlRepository.save(entity);

        // Act
        Optional<UrlEntity> found = urlRepository.findByShortUrl("abc123");

        // Assert
        assertTrue(found.isPresent());
        assertEquals("https://example.com", found.get().getOriginalUrl());
    }
}