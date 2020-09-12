package org.cloudshop.authservice.domain.adapters;

import org.cloudshop.authservice.AuthServiceException;
import org.cloudshop.authservice.domain.AuthUser;
import org.cloudshop.authservice.domain.Token;
import org.cloudshop.authservice.domain.UserCredentials;
import org.cloudshop.authservice.jwt.JwtTokenUtil;
import org.cloudshop.authservice.ports.UserAuthenticates;
import org.cloudshop.authservice.ports.UserServiceClient;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Objects;

public class UserAuthenticatesAdapter implements UserAuthenticates {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserServiceClient userServiceClient;


    public UserAuthenticatesAdapter(AuthenticationManager authenticationManager,
            JwtTokenUtil jwtTokenUtil,
            UserServiceClient userServiceClient) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userServiceClient = userServiceClient;
    }

    @Override
    public Token authenticate(UserCredentials userCredentials) throws AuthServiceException {
        authenticate(userCredentials.getEmail(), userCredentials.getPassword());

        final AuthUser userDetails = this.userServiceClient.getUserByEmail(userCredentials.getEmail());
        final String token = jwtTokenUtil.generateToken(userDetails);

        return new Token(token);
    }

    private void authenticate(String username, String password) throws AuthServiceException {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        try {
            this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new AuthServiceException("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new AuthServiceException("INVALID_CREDENTIALS", e);
        }
    }
}


