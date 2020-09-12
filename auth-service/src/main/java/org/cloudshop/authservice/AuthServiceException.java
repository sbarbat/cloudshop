package org.cloudshop.authservice;

public class AuthServiceException extends Exception {
    public AuthServiceException(String msg, Exception e) {
        super(msg, e);
    }
}
