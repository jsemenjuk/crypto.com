package com.crypto.demo.api;

import com.crypto.demo.api.GetCandlestickResponse;
import com.crypto.demo.api.GetTradesResponse;
import com.crypto.demo.model.Interval;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class CryptoApiClient {

    private final String baseUrl;

    public CryptoApiClient(@Value("${crypto.api.baseurl}") String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Mono<GetTradesResponse> getTrades(String instrumentName) {
        return buildClient().get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/v2/public/get-trades")
                                .queryParam("instrument_name", instrumentName)
                                .build())
                .retrieve().bodyToMono(GetTradesResponse.class);
    }

    public Mono<GetCandlestickResponse> getCandlestick(String instrumentName, Interval interval) {
        return buildClient().get()
                .uri(uriBuilder ->
                        uriBuilder.path("/v2/public/get-candlestick")
                                .queryParam("instrument_name", instrumentName)
                                .queryParam("timeframe", interval.getLabel())
                                .build())
                .retrieve()
                .bodyToMono(GetCandlestickResponse.class);
    }


    private WebClient buildClient() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }
}
