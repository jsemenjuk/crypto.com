package com.crypto.demo.model;

import com.crypto.demo.deserializer.BigDecimal10DpDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Candlestick {

    @JsonProperty("t")
    private long timestamp;

    @JsonProperty("o")
    @JsonDeserialize(using = BigDecimal10DpDeserializer.class)
    private BigDecimal openPrice;

    @JsonProperty("h")
    @JsonDeserialize(using = BigDecimal10DpDeserializer.class)
    private BigDecimal highPrice;

    @JsonProperty("l")
    @JsonDeserialize(using = BigDecimal10DpDeserializer.class)
    private BigDecimal lowPrice;

    @JsonProperty("c")
    @JsonDeserialize(using = BigDecimal10DpDeserializer.class)
    private BigDecimal closePrice;

    @JsonProperty("v")
    @JsonDeserialize(using = BigDecimal10DpDeserializer.class)
    private BigDecimal volume;

}

