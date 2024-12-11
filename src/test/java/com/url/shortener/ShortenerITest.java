package com.url.shortener;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.url.shortener.model.dto.UrlRequest;

@SpringBootTest
@AutoConfigureMockMvc
class ShortenerITest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testShortenUrl() throws Exception {
        UrlRequest request = new UrlRequest("https://test-example.com");
        mockMvc.perform(post("/api/v1/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").exists());
    }

    @Test
    void testResolveUrl() throws Exception {
        // First, shorten the URL
        UrlRequest request = new UrlRequest("https://test-example.com");
        String response = mockMvc.perform(post("/api/v1/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Extract the shortened URL hash from the response
        String shortUrlHash = objectMapper.readTree(response).get("url").asText();

        // Test resolving the shortened URL
        mockMvc.perform(get("/api/v1/" + shortUrlHash))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").value("https://example.com"));
    }
}