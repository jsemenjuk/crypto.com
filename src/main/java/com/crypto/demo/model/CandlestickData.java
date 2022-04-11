package com.crypto.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandlestickData {

    @JsonProperty("instrument_name")
    private String instrumentName;
    private String interval;
    private List<Candlestick> data;

}
