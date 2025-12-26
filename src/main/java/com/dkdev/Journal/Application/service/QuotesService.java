package com.dkdev.Journal.Application.service;

import com.dkdev.Journal.Application.api.response.QuotesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class QuotesService {
    @Value("${quote.api.key}")
    private String apiKey ;

    private final static String API = "https://api.api-ninjas.com/v2/randomquotes" ;

    @Autowired
    private RestTemplate restTemplate ;

    public QuotesResponse[] getQuote() {
        HttpHeaders headers = new HttpHeaders() ;
        headers.set("x-api-key" , apiKey);
        HttpEntity request = new HttpEntity(headers) ;
        ResponseEntity<QuotesResponse[]> response = restTemplate.exchange(API, HttpMethod.GET, request, QuotesResponse[].class);
        QuotesResponse[] body = response.getBody();
        return body ;
    }
}
