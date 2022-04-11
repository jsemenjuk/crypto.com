package com.crypto.demo.api;

import com.crypto.demo.model.CandlestickData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetCandlestickResponse {

    private int code;
    private String method;
    private CandlestickData result;

}
