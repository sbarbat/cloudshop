package org.cloudshop.authservice.domain;

import lombok.Data;

@Data
public class UserCredentials {
    private String email;
    private String password;
}
