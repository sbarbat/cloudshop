package org.cloudshop.authservice.rest;

import org.cloudshop.authservice.AuthServiceException;
import org.cloudshop.authservice.domain.Token;
import org.cloudshop.authservice.domain.UserCredentials;
import org.cloudshop.authservice.ports.UserAuthenticates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    private final UserAuthenticates userAuthenticates;

    @Autowired
    public AuthenticationController(UserAuthenticates userAuthenticates) {
        this.userAuthenticates = userAuthenticates;
    }

    @PostMapping("/authenticate")
    @ResponseBody
    public Token generateAuthenticationToken(@RequestBody UserCredentials userCredentials)
            throws AuthServiceException {
        return this.userAuthenticates.authenticate(userCredentials);
    }

    @GetMapping("/secure")
    @ResponseBody
    public String secure() {
        return "Logged in!";
    }

}
