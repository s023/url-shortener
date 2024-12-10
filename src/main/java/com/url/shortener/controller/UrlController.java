package com.url.shortener.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.url.shortener.model.dto.UrlRequest;
import com.url.shortener.model.dto.UrlResponse;
import com.url.shortener.service.UrlService;


@RestController
@RequestMapping("/api/v1")
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/shorten")
    public ResponseEntity<UrlResponse> shortenUrl(@RequestBody UrlRequest request) {
        String shortUrl = urlService.shortenUrl(request.originalUrl());
        return ResponseEntity.ok(new UrlResponse(shortUrl));
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<UrlResponse> resolveUrl(@PathVariable String shortUrl) {
        String originalUrl = urlService.resolveUrl(shortUrl);
        return ResponseEntity.ok(new UrlResponse(originalUrl));
    }
}
