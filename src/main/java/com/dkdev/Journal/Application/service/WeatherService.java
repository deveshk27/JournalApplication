package com.dkdev.Journal.Application.service;

import com.dkdev.Journal.Application.api.response.WeatherResponse;
import com.dkdev.Journal.Application.cache.Appcache;
import com.dkdev.Journal.Application.constants.Placeholders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WeatherService {
    @Value("${weather.api.key}")
    private String apiKey ;

    @Autowired
    private Appcache appcache ;

    @Autowired
    private RestTemplate restTemplate ;

    public WeatherResponse getWeather(String city) {
        String finalAPI = appcache.AppCache.get(Appcache.keys.WEATHER_API.toString()).replace(Placeholders.API_KEY, apiKey).replace(Placeholders.CITY , city) ;
        ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPI, HttpMethod.GET, null, WeatherResponse.class);
        WeatherResponse body = response.getBody();
        return body ;
    }
}
