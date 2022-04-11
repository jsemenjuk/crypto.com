package com.crypto.demo.api;

import com.crypto.demo.model.Trade;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetTradesResponseResult {

    @JsonProperty("instrument_name")
    private String instrumentName;

    private List<Trade> data;

}
