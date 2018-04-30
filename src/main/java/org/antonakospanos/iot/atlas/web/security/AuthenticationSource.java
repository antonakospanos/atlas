package org.antonakospanos.iot.atlas.web.security;

import org.antonakospanos.iot.atlas.web.exceptions.AtlasException;
import org.springframework.security.authentication.AuthenticationDetailsSource;

import javax.servlet.http.HttpServletRequest;

public class AuthenticationSource implements AuthenticationDetailsSource<HttpServletRequest, AuthenticationDetails> {

    public static final String API_KEY = "Bearer 123";

    @Override
    public AuthenticationDetails buildDetails(HttpServletRequest httpRequest) {
        AuthenticationDetails authDetails = new AuthenticationDetails();
        String apiKey = parseHttpHeader(httpRequest, "Authorization", API_KEY, "Invalid Authorization header!");
        authDetails.setApiKey(apiKey);

        return authDetails;
    }

    /**
     * Parses the requested HTTP header
     *
     * @param httpRequest the http request
     * @param header the HTTP header
     * @param expectedValue The expected HTTP header value
     * @param error the error message in case of absence
     *
     * @return the API Key
     */
    private String parseHttpHeader(HttpServletRequest httpRequest, String header, String expectedValue, String error) {
        String value = httpRequest.getHeader(header);
        if (!expectedValue.equals(value)) {
            throw new AtlasException(error);
        }

        return value;
    }
}
