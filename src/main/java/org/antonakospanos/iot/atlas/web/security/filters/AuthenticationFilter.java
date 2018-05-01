package org.antonakospanos.iot.atlas.web.security.filters;

import org.antonakospanos.iot.atlas.web.security.authentication.AtlasAuthenticationDetailsSource;
import org.antonakospanos.iot.atlas.web.security.authentication.AtlasAuthenticationToken;
import org.antonakospanos.iot.atlas.web.security.authentication.AuthenticationDetails;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class AuthenticationFilter extends GenericFilterBean {

    private AtlasAuthenticationDetailsSource atlasAuthenticationDetailsSource;
    private AuthenticationManager authenticationManager;

    public AuthenticationFilter(AuthenticationManager authManager) {
        this.authenticationManager = authManager;
        this.atlasAuthenticationDetailsSource = new AtlasAuthenticationDetailsSource();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        if (authenticate(httpRequest)) {
            chain.doFilter(request, response);
        }
    }

    private boolean authenticate(HttpServletRequest request) {
        boolean authenticated = false;

        try {
            // Build AuthenticationDetails parsing HTTP Authorization header
            AuthenticationDetails authDetails = this.atlasAuthenticationDetailsSource.buildDetails(request);

            // Build AtlasAuthenticationToken using AuthenticationDetails
            AtlasAuthenticationToken authToken = new AtlasAuthenticationToken();
            authToken.setDetails(authDetails);

            // Authenticate request using the list of the authentication manager's authentication providers (AtlasAuthenticationProvider)
            Authentication authResult = this.authenticationManager.authenticate(authToken);
            SecurityContextHolder.getContext().setAuthentication(authResult);
            authenticated = true;

        } catch (AuthenticationException failed) {
            SecurityContextHolder.clearContext();
        }

        return authenticated;
    }
}
