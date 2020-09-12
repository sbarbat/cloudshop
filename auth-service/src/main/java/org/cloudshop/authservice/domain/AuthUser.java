package org.cloudshop.authservice.domain;

import lombok.Data;

@Data
public class AuthUser {
    private String id;
    private String username;
    private String password;
    private String email;
    private String firstname;
    private String lastname;
}
