package org.antonakospanos.iot.atlas.web.exceptions;

public class AtlasException extends RuntimeException {

    public AtlasException() {
    }

    public AtlasException(String message) {
        super(message);
    }
}
