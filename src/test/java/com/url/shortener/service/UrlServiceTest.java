package com.url.shortener.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.url.shortener.model.entity.UrlEntity;
import com.url.shortener.model.repository.UrlRepository;

class UrlServiceTest {
    private final UrlRepository urlRepository = mock(UrlRepository.class); // Mocked repository
    private final UrlService urlService = new UrlService(urlRepository);   // Service under test

    @Test
    void testShortenUrlWhenUrlAlreadyExists() {
        // Arrange
        String originalUrl = "https://example.com";
        String shortUrl = "abc123";
        UrlEntity existingEntity = new UrlEntity(1L, originalUrl, shortUrl);
        when(urlRepository.findByOriginalUrl(originalUrl)).thenReturn(Optional.of(existingEntity));

        // Act
        String result = urlService.shortenUrl(originalUrl);

        // Assert
        assertEquals(shortUrl, result);
        verify(urlRepository, times(1)).findByOriginalUrl(originalUrl);
        verify(urlRepository, never()).save(any()); // No new entity should be saved
    }

    @Test
    void testShortenUrlWhenUrlDoesNotExist() {
        // Arrange
        String originalUrl = "https://example.com";
        when(urlRepository.findByOriginalUrl(originalUrl)).thenReturn(Optional.empty());
        when(urlRepository.save(any(UrlEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        String result = urlService.shortenUrl(originalUrl);

        // Assert
        assertEquals(8, result.length());
        verify(urlRepository, times(1)).save(any(UrlEntity.class));
    }

    @Test
    void testResolveUrlWhenShortUrlExists() {
        // Arrange
        String shortUrl = "abc123";
        String originalUrl = "https://example.com";
        UrlEntity entity = new UrlEntity(1L, originalUrl, shortUrl);
        when(urlRepository.findByShortUrl(shortUrl)).thenReturn(Optional.of(entity));

        // Act
        String result = urlService.resolveUrl(shortUrl);

        // Assert
        assertEquals(originalUrl, result);
        verify(urlRepository, times(1)).findByShortUrl(shortUrl);
    }

    @Test
    void testResolveUrlWhenShortUrlDoesNotExist() {
        // Arrange
        String shortUrl = "abc123";
        when(urlRepository.findByShortUrl(shortUrl)).thenReturn(Optional.empty());

        // Act & Assert
        try {
            urlService.resolveUrl(shortUrl);
        } catch (RuntimeException e) {
            assertEquals("URL not found!", e.getMessage());
        }
        verify(urlRepository, times(1)).findByShortUrl(shortUrl);
    }

}