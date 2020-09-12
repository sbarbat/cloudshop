package org.cloudshop.authservice.ports;

import org.cloudshop.authservice.AuthServiceException;
import org.cloudshop.authservice.domain.Token;
import org.cloudshop.authservice.domain.UserCredentials;

public interface UserAuthenticates {

    Token authenticate(UserCredentials userCredentials) throws AuthServiceException;
}
