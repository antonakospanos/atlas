package org.antonakospanos.iot.atlas.web.support;

import org.antonakospanos.iot.atlas.web.configuration.SecurityConfiguration;
import org.antonakospanos.iot.atlas.web.security.exception.AtlasAuthenticationException;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

public class SecurityHelper {


    /**
     * Checks if is url protected.
     *
     * @param url the url
     * @return true, if is url protected
     */
    public static boolean isPublicApiUrl(String url) {
        boolean result = false;

        for (String pattern : SecurityConfiguration.getPublicApis()) {
            if (url.matches(pattern)) {
                result = true;
                break;
            }
        }

        return result;
    }

    /**
     * Gets the request url.
     *
     * @param request the request
     * @return the request url
     */
    public static String getRequestUrl(HttpServletRequest request) {
        String result = request.getServletPath();

        if (StringUtils.isBlank(result)) {
            result = request.getPathInfo();
        }

        return result;
    }

    /**
     * Parses the HTTP Authorization header and returns, if found, the user's access token
     *
     * @param authorizationHeader
     * @return The UUID access-token
     */
    public static String getAccessToken(String authorizationHeader) {
        return StringUtils.substringAfter(authorizationHeader, "Bearer ");
    }


    /**
     * Parses the HTTP Authorization header and returns, if found, the user's access token
     *
     * @param authorizationHeader
     * @return The UUID access-token
     */
    public static UUID getUuidToken(String authorizationHeader) {
        String token =  getAccessToken(authorizationHeader);
        return convertAccessToken(token);
    }

    /**
     * Converts the parsed access-token, from HTTP Authorization header, to UUID
     *
     * @param token
     * @return The UUID access-token
     */
    public static UUID convertAccessToken(String token) {
        UUID accessToken;
        try {
            accessToken = UUID.fromString(token);
        } catch (Exception e) {
            throw new AtlasAuthenticationException("Invalid HTTP Authorization header Bearer: " + token);
        }
        return accessToken;
    }
}
