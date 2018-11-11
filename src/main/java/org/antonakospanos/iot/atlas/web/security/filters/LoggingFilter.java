package org.antonakospanos.iot.atlas.web.security.filters;

import org.antonakospanos.iot.atlas.web.support.ApiLoggingHelper;
import org.antonakospanos.iot.atlas.web.support.MutableHttpServletRequest;
import org.antonakospanos.iot.atlas.web.support.SecurityHelper;
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
    private static final String CONTENT_TYPE_BINARY = "multipart/form-data";

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
     * javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String url = SecurityHelper.getRequestUrl((HttpServletRequest) request);

        if (!SecurityHelper.isPublicApiUrl(url)) {
            // Do not log request/response
            chain.doFilter(request, response);
        } else {
            // Wrap servlet request to allow multiple reads on HTTP payload
            HttpServletRequest httpRequest = new MutableHttpServletRequest((HttpServletRequest) request);

            // Log request
            String reqContentType = httpRequest.getHeader("Content-Type");
            boolean isReqBinary = reqContentType != null && reqContentType.startsWith(CONTENT_TYPE_BINARY);
            String uid = UUID.randomUUID().toString();
            LOGGER.debug("Request: " + uid + "\n" + ApiLoggingHelper.serializeRequest(httpRequest, isReqBinary));

            // Initialize HTTP response with empty output stream
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            HttpServletResponse httpResponse = createHttpResponse(response, baos);

            // Proceed with filters (with wrapped request + response)
            chain.doFilter(httpRequest, httpResponse);

            // Log response
            String resContentType = httpResponse.getHeader("Content-Type");
            boolean isResBinary = resContentType != null && resContentType.startsWith(CONTENT_TYPE_BINARY);
            LOGGER.debug("Response: " + uid + "\n" + ApiLoggingHelper.serializeResponse(httpResponse, baos, isResBinary));
        }
    }


    private HttpServletResponse createHttpResponse(ServletResponse response, ByteArrayOutputStream baos) {
        final PrintStream ps = new PrintStream(baos);
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        httpResponse = new HttpServletResponseWrapper(httpResponse) {
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

        return httpResponse;
    }
}
