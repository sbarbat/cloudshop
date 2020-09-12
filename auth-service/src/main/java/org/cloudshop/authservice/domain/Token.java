package org.cloudshop.authservice.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Token {
    private final String token;

    public String getToken() {
        return this.token;
    }
}
