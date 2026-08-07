package com.short_url.controller;

import org.springframework.web.bind.annotation.RestController;

import com.short_url.dto.UrlRequestDto;
import com.short_url.dto.UrlResponseDto;
import com.short_url.service.UrlService;
import com.short_url.entity.Url;

import lombok.RequiredArgsConstructor;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




@RestController

@RequiredArgsConstructor
@RequestMapping("/url")
public class UrlController {
    
    private final UrlService urlService;
    
    
    @PostMapping("/shorten")
    public ResponseEntity<UrlResponseDto> shortenUrl (@RequestBody UrlRequestDto request) {
        String link = request.originalUrl();

        Url savedUrl= urlService.shortenUrl(link);

        UrlResponseDto response = new UrlResponseDto(
            savedUrl.getOriginalUrl(),
            savedUrl.getShortCode(),
            savedUrl.getAcessCount(),
            savedUrl.getCreatedAt()
        );
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PutMapping("update/{shortCode}")
    public ResponseEntity<UrlResponseDto> updateShortenUrl(@PathVariable String shortCode, @RequestBody UrlRequestDto request) {

        Url updatedLink = urlService.updateUrl(shortCode, request.originalUrl());
        
        UrlResponseDto response = new UrlResponseDto(
            updatedLink.getOriginalUrl(),
            updatedLink.getShortCode(),
            updatedLink.getAcessCount(),
            updatedLink.getCreatedAt()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode) {
        String originalLink = urlService.getOriginalUrl(shortCode);
        URI goToUri= URI.create(originalLink);
        return ResponseEntity.status(HttpStatus.FOUND).location(goToUri).build();
    }
    
    @DeleteMapping("/delete/{shortCode}")
    public ResponseEntity<Void> delete(@PathVariable String shortCode){
        urlService.deleteUrl(shortCode);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
