package com.crypto.demo.model;

import com.crypto.demo.deserializer.BigDecimal10DpDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Trade {

    @JsonProperty("i")
    private String instrumentName;

    @JsonProperty("p")
    @JsonDeserialize(using = BigDecimal10DpDeserializer.class)
    private BigDecimal price;

    @JsonProperty("q")
    @JsonDeserialize(using = BigDecimal10DpDeserializer.class)
    private BigDecimal quantity;

    @JsonProperty("s")
    private Side side;

    @JsonProperty("d")
    private BigInteger id;

    @JsonProperty("t")
    private long timestamp;

}
