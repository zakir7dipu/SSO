package com.bizzsol.sso.sso.security;

public class ApplicationNotFoundException extends RuntimeException{
    public ApplicationNotFoundException(String appName) {
        super("Application not found: " + appName);
    }
}
