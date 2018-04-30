package org.antonakospanos.iot.atlas.web.security.filters;

import org.antonakospanos.iot.atlas.web.support.LoggingUtils;
import org.apache.commons.io.output.TeeOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.DelegatingServletOutputStream;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.UUID;

public class LoggingFilter extends GenericFilterBean {

    private final Logger LOGGER = LoggerFactory.getLogger(LoggingFilter.class);

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
     * javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // Cast servlet req/res to HTTP res/res
        HttpServletRequest httpRequest = ((HttpServletRequest) request);
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String uid = UUID.randomUUID().toString();

        // Log request
        LOGGER.debug("Request: " + uid + "\n" + LoggingUtils.serializeRequest(httpRequest));

        // Initialize HTTP response with empty output stream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        httpResponse = getHttpResponse(httpResponse, baos);

        // Proceed with filters (with wrapped response + request)
        chain.doFilter(httpRequest, httpResponse);

        // Log response
        LOGGER.debug("Response: " + uid + "\n" + LoggingUtils.serializeResponse(httpResponse, baos));
    }


    private HttpServletResponse getHttpResponse(HttpServletResponse response, ByteArrayOutputStream baos) {
        final PrintStream ps = new PrintStream(baos);

        response = new HttpServletResponseWrapper(response) {
            @Override
            public ServletOutputStream getOutputStream() throws IOException {
                return new DelegatingServletOutputStream(new TeeOutputStream(super.getOutputStream(), ps));
            }

            @Override
            public PrintWriter getWriter() throws IOException {
                return new PrintWriter(
                        new DelegatingServletOutputStream(new TeeOutputStream(super.getOutputStream(), ps)));
            }
        };

        return response;
    }
}
