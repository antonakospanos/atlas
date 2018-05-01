package org.antonakospanos.iot.atlas.web.exception;

import org.springframework.security.core.AuthenticationException;

public class AtlasAuthenticationException extends AuthenticationException {

    public AtlasAuthenticationException(String message) {
        super(message);
    }
}
