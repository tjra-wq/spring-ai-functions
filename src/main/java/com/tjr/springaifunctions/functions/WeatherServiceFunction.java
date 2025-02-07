package com.tjr.springaifunctions.functions;

import com.tjr.springaifunctions.model.WeatherRequest;
import com.tjr.springaifunctions.model.WeatherResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestClient;

import java.util.function.Function;

@RequiredArgsConstructor
public class WeatherServiceFunction implements Function<WeatherRequest, WeatherResponse> {
    public static final String WEATHER_URL = "https://api.api-ninjas.com/v1/weather";
    private final String apiNinjasKey;

    @Override
    public WeatherResponse apply(WeatherRequest weatherRequest) {
        RestClient restClient = RestClient.builder()
                .baseUrl(WEATHER_URL)
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.set("X-Api-Key", apiNinjasKey);
                    httpHeaders.set("Accept", "application/json");
                    httpHeaders.set("Content-Type", "application/json");
                }).build();
        return restClient.get().uri(uriBuilder -> {
            System.out.println("Building URI for weather request with coordinates: "
                    + "lat=" + weatherRequest.latitude() + ", lon=" + weatherRequest.longitude());

            uriBuilder.queryParam("lat", weatherRequest.latitude())
                    .queryParam("lon", weatherRequest.longitude());

            return uriBuilder.build();
        }).retrieve().body(WeatherResponse.class);
    }
}
