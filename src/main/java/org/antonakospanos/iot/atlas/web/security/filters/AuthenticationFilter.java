package org.antonakospanos.iot.atlas.web.security.filters;

import org.antonakospanos.iot.atlas.web.security.authentication.AtlasAuthenticationDetailsSource;
import org.antonakospanos.iot.atlas.web.security.authentication.AuthenticationDetails;
import org.antonakospanos.iot.atlas.web.security.authentication.AuthenticationToken;
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
import javax.servlet.http.HttpServletResponse;
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
		boolean proceed = true;

		if (SecurityContextHolder.getContext().getAuthentication() == null) {
			proceed = authenticate(request, response, chain);
		}

		if (proceed) {
			chain.doFilter(request, response);
		}
	}

	private boolean authenticate(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		boolean proceed = false;

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

			try {
				AuthenticationToken authToken = new AuthenticationToken();
				AuthenticationDetails details = this.atlasAuthenticationDetailsSource.buildDetails(httpRequest);
				authToken.setDetails(details);
				authToken.setCredentials(details.getAccessToken());
				Authentication authResult = this.authenticationManager.authenticate(authToken);
				SecurityContextHolder.getContext().setAuthentication(authResult);
				proceed = true;

			} catch (AuthenticationException failed) {
				SecurityContextHolder.clearContext();
			}

		return proceed;
	}
}
