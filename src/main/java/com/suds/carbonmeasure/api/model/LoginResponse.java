package com.suds.carbonmeasure.api.model;

import lombok.Data;

@Data
public class LoginResponse {

    private String token;
    private String username;
}
