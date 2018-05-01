package org.antonakospanos.iot.atlas.web.security.authentication;

import org.antonakospanos.iot.atlas.dao.model.Account;
import org.antonakospanos.iot.atlas.service.AccountService;
import org.antonakospanos.iot.atlas.web.configuration.SecurityConfiguration;
import org.antonakospanos.iot.atlas.web.exception.AtlasAuthenticationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
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
	 * @param authenticationRequest the authenticationRequest
	 *
	 * @return the authenticationRequest
	 * @throws AuthenticationException the authenticationRequest exception
	 */
	@Override
	public Authentication authenticate(Authentication authenticationRequest) throws AuthenticationException {
		AuthenticationDetails authDetails = (AuthenticationDetails) authenticationRequest.getDetails();

		Account account = null;
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		String token = authDetails.getAccessToken();
		if (StringUtils.isBlank(token)) {
			// No token, grant only /devices API
			authorities.add(new SimpleGrantedAuthority(SecurityConfiguration.ROLE_DEVICE));
			authenticationRequest.setAuthenticated(true);
		} else if (adminAccessToken.equals(token)) {
			// Admin token
			authorities.add(new SimpleGrantedAuthority(SecurityConfiguration.ROLE_ADMIN));
			authenticationRequest.setAuthenticated(true);
		} else {
			// Validate that user's access token is listed in Account table
			authorities.add(new SimpleGrantedAuthority(SecurityConfiguration.ROLE_APPLICATION));
			UUID uuidToken;
			try {
				uuidToken = UUID.fromString(token);
			} catch (Exception e) {
				throw new AtlasAuthenticationException("Invalid HTTP Authorization header Bearer: " + token);
			}

			account = accountService.find(uuidToken);
			boolean authenticated = account != null;
			authenticationRequest.setAuthenticated(authenticated);
		}

		AtlasAuthenticationToken authenticationResponse = new AtlasAuthenticationToken(authorities);
		authenticationResponse.setPrincipal(account);
		authenticationResponse.setDetails(authenticationRequest.getDetails());
		authenticationResponse.setCredentials(token);

		return authenticationResponse;
	}

	/**
	 * Authentication class supported
	 *
	 * @param authentication the authentication
	 * @return true, if successful
	 */
	@Override
	public boolean supports(Class<?> authentication) {
		return AtlasAuthenticationToken.class.isAssignableFrom(authentication);
	}
}