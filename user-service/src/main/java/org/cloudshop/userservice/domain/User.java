package org.cloudshop.userservice.domain;

import lombok.Data;

@Data
public class User {
    private String id;
    private String username;
    private String password;
    private String email;
    private String firstname;
    private String lastname;
}
