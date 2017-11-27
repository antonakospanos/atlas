package org.antonakospanos.iot.atlas.web.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

import java.util.UUID;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private static final String[] FREE_API = new String[]{"/version", "/docs.*", "/health.*", "/ehcache.*", "/metrics.*"};
	private static final String[] ACTIONS_API = new String[]{"/actions/.*"};
	private static final String[] EVENTS_API = new String[]{"/events/.*"};
	private static final String[] DEVICES_API = new String[]{"/devices/.*"};

	//	@Value("${application.roles.admin}")
	private static final String ROLE_ADMIN = "app_user";

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf()
				.disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.authorizeRequests()
				.regexMatchers(ACTIONS_API).hasAuthority(ROLE_ADMIN)
				.regexMatchers(EVENTS_API).permitAll()
				.regexMatchers(DEVICES_API).permitAll()
				.regexMatchers(FREE_API).permitAll()
				.anyRequest().authenticated();

		http.headers().frameOptions().disable();
		http.headers().httpStrictTransportSecurity().disable();
		http.headers().xssProtection().disable();

		AnonymousAuthenticationFilter anonymousAuthenticationFilter = new AnonymousAuthenticationFilter(UUID.randomUUID().toString());
		http.anonymous().authenticationFilter(anonymousAuthenticationFilter);

	}
}