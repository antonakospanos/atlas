package org.antonakospanos.iot.atlas.web.security.authentication;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Collections;

public class AuthenticationToken extends AbstractAuthenticationToken {

    public AuthenticationToken() {
        super(Collections.emptyList());
    }

    private Object token;
    private Object user;

    public void setCredentials(Object token) {
        this.token = token;
    }

    public void setPrincipal(Object user) {
        this.user = user;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return user;
    }
}
