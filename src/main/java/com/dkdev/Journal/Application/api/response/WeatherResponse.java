package com.dkdev.Journal.Application.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class WeatherResponse {
    private Current current ;

    @Getter
    @Setter
    public class Current{
        @JsonProperty("weather_descriptions")
        private List<String> weatherDescriptions ;

        private int temperature;
        private int feelslike;
    }

}
