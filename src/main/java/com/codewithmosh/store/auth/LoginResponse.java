package com.codewithmosh.store.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginResponse {
    public Jwt accessToken;
    public Jwt refreshToken;
}
