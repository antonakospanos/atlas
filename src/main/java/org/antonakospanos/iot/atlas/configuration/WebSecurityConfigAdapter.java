package org.antonakospanos.iot.atlas.configuration;

import com.codahale.metrics.MetricRegistry;
import com.upstreamsystems.hydra.enums.AuthenticationLevel;
import com.upstreamsystems.hydra.web.security.filter.*;
import com.upstreamsystems.hydra.web.security.service.impl.CustomAuthenticationProvider;
import com.upstreamsystems.hydra.web.security.util.CustomAuthenticationDetailsSource;
import com.upstreamsystems.hydra.web.security.util.CustomBasicAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.util.List;
import java.util.UUID;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class WebSecurityConfigAdapter extends WebSecurityConfigurerAdapter {

	@Autowired
	private CustomAuthenticationProvider customAuthenticationProvider;

	@Autowired
	private CustomBasicAuthenticationEntryPoint customBasicAuthenticationEntryPoint;

	@Autowired
	private MetricRegistry metricRegistry;

	private static final String[] UNPROTECTED_NON_API_URLS_REGEX = new String[] { "/version", "/docs.*", "/v2/api-docs.*",
			"/ehcache.*", "/health.*", "/metrics.*", "/api/v1/health", "/api/v2/health"};

	private static final String[] PROTECTED_NON_API_URLS_REGEX = new String[] { "/mgmt.*", "/v2/api-docs\\?group=mgmt.*", "/docs/mgmt\\.html.*"};
	
	private static final String[] UNPROTECTED_V1_URLS = new String[] { "/api/v1/game/user/subscribe",
			"/api/v1/game/user/password", "/api/v1/game/user/generatecredentials" };
	
	private static final String[] UNPROTECTED_V2_URLS = new String[] { "/api/v2/subscription/prompt",
			"/api/v2/account/credentials/remind", "/api/v2/account/credentials/generate", "/api/v2/event/postguest" };
	
	private static final String[] V1_URL_REGEX = new String[] { "/api/v1/.*" };
	
	private static final String[] V2_URL_REGEX = new String[] { "/api/v2/.*" };
	
	private static final String[] UNPROTECTED_INTERNAL_API_URLS_REGEX = new String[] { "/internal/.*" };
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO: Filter out non-auth methods

		List<String> roles = AuthenticationLevel.getRoles();

		http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.authorizeRequests()
				// Unprotected API V1 calls
				.antMatchers(UNPROTECTED_V1_URLS).permitAll()
				// Unprotected API V2 calls
				.antMatchers(UNPROTECTED_V2_URLS).permitAll()
				// Protected non - api calls (mgt)
				.regexMatchers(PROTECTED_NON_API_URLS_REGEX).hasAuthority(AuthenticationLevel.ADMIN.getRole())
				// Unprotected non - api calls
				.regexMatchers(UNPROTECTED_NON_API_URLS_REGEX).permitAll()
				// Unprotected internal api calls
				.regexMatchers(UNPROTECTED_INTERNAL_API_URLS_REGEX).permitAll()
				// Protected API V1 + V2 calls
				.anyRequest().authenticated();

		http.headers().frameOptions().disable();
		http.headers().httpStrictTransportSecurity().disable();
		http.headers().xssProtection().disable();
		
		http.addFilterBefore(new CustomBasicAuthenticationFilter(authenticationManager()),
				BasicAuthenticationFilter.class);
		http.addFilterBefore(
				new AutomaticAuthenticationFilter(authenticationManager(), customBasicAuthenticationEntryPoint),
				CustomBasicAuthenticationFilter.class);
		http.addFilterBefore(
				new HeaderEnrichmentFilter(authenticationManager(), customBasicAuthenticationEntryPoint),
				AutomaticAuthenticationFilter.class);
		http.addFilterBefore(
				new OAuth2Filter(authenticationManager(), customBasicAuthenticationEntryPoint),
				HeaderEnrichmentFilter.class);
		http.addFilterBefore(new LegacyAPIUpgradeFilter(), OAuth2Filter.class);
		http.addFilterBefore(new CustomExceptionHandlerFilter(), LegacyAPIUpgradeFilter.class);
		http.addFilterBefore(new RequestUIDFilter(), CustomExceptionHandlerFilter.class);
		http.addFilterBefore(
				new MetricsFilter(metricRegistry),
				RequestUIDFilter.class);

		http.exceptionHandling().authenticationEntryPoint(customBasicAuthenticationEntryPoint);

		AnonymousAuthenticationFilter anonymousAuthenticationFilter = new AnonymousAuthenticationFilter(
				UUID.randomUUID().toString());
		anonymousAuthenticationFilter.setAuthenticationDetailsSource(new CustomAuthenticationDetailsSource());
		http.anonymous().authenticationFilter(anonymousAuthenticationFilter);

	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(customAuthenticationProvider);
	}

	public static String[] getUnprotectedNonApiUrlsRegex() {
		return UNPROTECTED_NON_API_URLS_REGEX;
	}

	public static String[] getProtectedNonApiUrlsRegex() {
		return PROTECTED_NON_API_URLS_REGEX;
	}

	public static String[] getUnprotectedV1Urls() {
		return UNPROTECTED_V1_URLS;
	}

	public static String[] getUnprotectedV2Urls() {
		return UNPROTECTED_V2_URLS;
	}

	public static String[] getV1UrlRegex() {
		return V1_URL_REGEX;
	}

	public static String[] getV2UrlRegex() {
		return V2_URL_REGEX;
	}

	public static String[] getUnprotectedInternalApiUrlsRegex() {
		return UNPROTECTED_INTERNAL_API_URLS_REGEX;
	}
	
}