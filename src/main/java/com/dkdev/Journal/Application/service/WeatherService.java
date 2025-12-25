package com.dkdev.Journal.Application.service;

import com.dkdev.Journal.Application.api.response.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component 
public class WeatherService {
    private static final String apiKey = "f00ed2468f691a95684508aaef540f36" ;

    private static final String API = "http://api.weatherstack.com/current?access_key=API_KEY&query=CITY" ;

    @Autowired
    private RestTemplate restTemplate ;

    public WeatherResponse getWeather(String city) {
        String finalAPI = API.replace("API_KEY" , apiKey).replace("CITY" , city) ;
        ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPI, HttpMethod.GET, null, WeatherResponse.class);
        WeatherResponse body = response.getBody();
        return body ;
    }
}
