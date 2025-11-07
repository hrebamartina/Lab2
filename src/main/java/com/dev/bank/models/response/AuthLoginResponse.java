package com.dev.bank.models.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AuthLoginResponse extends BaseResponse {
    private String token;
}