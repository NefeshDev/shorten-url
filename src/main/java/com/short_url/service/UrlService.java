package com.short_url.service;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;

import com.short_url.entity.Url;
import com.short_url.repo.UrlRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UrlService {
    private final UrlRepository urlRepository;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int CODE_LENGTH = 6;   

    private String generateRandomCode(){
        StringBuilder sb = new StringBuilder(CODE_LENGTH);

        for(int i = 0; i< CODE_LENGTH; i++){

            int randomIndex = ThreadLocalRandom.current().nextInt(CHARACTERS.length());

            sb.append(CHARACTERS.charAt(randomIndex));
        }

        return sb.toString();
    }

    public Url shortenUrl(String originalUrl){
        if(urlRepository.existsByOriginalUrl(originalUrl)){
            throw new RuntimeException("Url already exists!");
        }

        Url url = new Url();
        url.setOriginalUrl(originalUrl);

        String shortCode;
        do {
            shortCode = generateRandomCode();
        } while (urlRepository.existsByShortCode(shortCode));

        url.setShortCode(shortCode);

        return urlRepository.save(url);
    }

    public Url updateUrl(String shortCode, String newOriginalUrl){
        Url existUrl = urlRepository.findByShortCode(shortCode)
            .orElseThrow(() -> new RuntimeException("URL não encontrada para o código: " + shortCode));
        
        existUrl.setOriginalUrl(newOriginalUrl);

        return urlRepository.save(existUrl);
    }


    public String getOriginalUrl(String shortCode){
        Url url = urlRepository.findByShortCode(shortCode)
            .orElseThrow(() -> new RuntimeException("URL não encontrada para o código: " + shortCode));
        
        urlRepository.incrementAccessCount(shortCode);
        
        return url.getOriginalUrl();
    }

    public void deleteUrl(String shortCode){
        Url urlToDelete = urlRepository.findByShortCode(shortCode)
            .orElseThrow(() -> new RuntimeException("URL não encontrada para o código: " + shortCode));
        
        urlRepository.delete(urlToDelete);
    }
}
