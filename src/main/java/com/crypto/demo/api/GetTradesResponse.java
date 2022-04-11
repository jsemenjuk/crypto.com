package com.crypto.demo.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Documentation missing 'result' wrapper
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetTradesResponse {

    private int code;
    private String method;
    private GetTradesResponseResult result;

}
