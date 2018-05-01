package org.antonakospanos.iot.atlas.web.security.authentication;

import org.antonakospanos.iot.atlas.service.AccountService;
import org.antonakospanos.iot.atlas.web.exception.AtlasAuthenticationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.UUID;

/**
 * AtlasAuthenticationProvider.
 */
@Component
public class AtlasAuthenticationProvider implements AuthenticationProvider, Serializable {

	@Autowired
	private AccountService accountService;


	@Value("${application.roles.admin.access-token}")
	private String adminAccessToken;

	/**
	 * Authenticate requests to Atlas API
	 *
	 * @param authentication the authentication
	 *
	 * @return the authentication
	 * @throws AuthenticationException the authentication exception
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		AuthenticationDetails authDetails = (AuthenticationDetails) authentication.getDetails();

		// exclude PUT /devices/* and POST /events/heartbeat that are used by IoT devices
		// exclude POST /accounts (registration) and GET /accounts/id (authentication)

		String token = authDetails.getAccessToken();
		if (StringUtils.isBlank(token)) {
			throw new AtlasAuthenticationException("API Key not found!");
		} else if (adminAccessToken.equals(token)) {
			// Admin token exception
			authentication.setAuthenticated(true);
		} else {
			// Validate that user's access token is listed in Account table
			UUID uuidToken = UUID.fromString(token);
			boolean authenticated = accountService.exists(uuidToken);
			authentication.setAuthenticated(authenticated);
		}

		return authentication;
	}

	/**
	 * Authentication class supported
	 *
	 * @param authentication the authentication
	 * @return true, if successful
	 */
	@Override
	public boolean supports(Class<?> authentication) {
		return AuthenticationToken.class.isAssignableFrom(authentication);
	}
}