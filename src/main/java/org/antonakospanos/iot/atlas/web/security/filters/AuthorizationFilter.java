package org.antonakospanos.iot.atlas.web.security.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class AuthorizationFilter extends GenericFilterBean {

    public static final String API_KEY = "Bearer aW90YWNzeXN0ZW1z"; // iotacsystems base64 encoded

    /**
     * The logger.
     */
    private final Logger LOGGER = LoggerFactory.getLogger(AuthorizationFilter.class);

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
     * javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = ((HttpServletRequest) request);
        boolean valid = true; // TODO: authorizeRequest(httpRequest);

        if (valid) {
            chain.doFilter(request, response);
        } else {
            LOGGER.error("Invalid Authorization header!");
        }
    }

    /**
     * Authorizes the request to Atlas API
     *
     * @param httpRequest the http request
     */
    private boolean authorizeRequest(HttpServletRequest httpRequest) {
        boolean result =false;
        String value = httpRequest.getHeader("Authorization");
        if (API_KEY.equals(value)) {
            result = true;
        }

        return result;
    }
}
