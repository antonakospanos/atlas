package org.antonakospanos.iot.atlas.web.security.authentication;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationDetailsSource;

import javax.servlet.http.HttpServletRequest;

public class AtlasAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest, AuthenticationDetails> {

    @Override
    public AuthenticationDetails buildDetails(HttpServletRequest httpRequest) {
        AuthenticationDetails authDetails = new AuthenticationDetails();
        String authorizationHeader = httpRequest.getHeader("Authorization");

        if (StringUtils.isNotBlank(authorizationHeader)) {
            String accessToken = StringUtils.substringAfter(authorizationHeader, "Bearer ");
            authDetails.setAccessToken(accessToken);
        }

        return authDetails;
    }
}
