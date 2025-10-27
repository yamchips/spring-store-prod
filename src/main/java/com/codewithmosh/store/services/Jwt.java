package com.codewithmosh.store.services;


import com.codewithmosh.store.users.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.util.Date;

public class Jwt {

    private final SecretKey key;

    private final Claims claims;

    public Jwt (Claims claims, SecretKey key) {
        this.key = key;
        this.claims = claims;
    }

    public boolean isExpired() {
        return this.claims.getExpiration().before(new Date());
    }

    public Long getUserId() {
        return Long.valueOf(this.claims.getSubject());
    }

    public Role getRole() {
        return Role.valueOf(this.claims.get("role", String.class));
    }

    @Override
    public String toString() {
        return Jwts.builder()
                .claims(this.claims)
                .signWith(this.key)
                .compact();
    }

}
