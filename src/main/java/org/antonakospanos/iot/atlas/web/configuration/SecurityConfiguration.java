package org.antonakospanos.iot.atlas.web.configuration;

import org.antonakospanos.iot.atlas.web.security.authentication.AtlasAuthenticationProvider;
import org.antonakospanos.iot.atlas.web.security.filters.AuthenticationFilter;
import org.antonakospanos.iot.atlas.web.security.filters.ExceptionHandlerFilter;
import org.antonakospanos.iot.atlas.web.security.filters.LoggingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private static final String[] SWAGGER_WHITELIST_REGEX = {"/swagger-resources.*", "/swagger-ui.html", "docs/api.html",
			"/v2/api-docs.*", "/docs.*", "/webjars.*", "/configuration/ui", "/configuration/security"};

	private static final String[] ATLAS_WHITELIST_REGEX = new String[]{"/", "/version", "/health", "/ehcache.*", "/metrics.*"};

	private static final String EVENTS_API = "/events/**";
	private static final String DEVICES_API = "/devices/**";
	private static final String ACCOUNTS_API = "/accounts/**";
	private static final String ACTIONS_API = "/actions/**";

	private static final String[] ADMIN_API = {"/admin/**"};
	private static final String[] DEVICE_API = { EVENTS_API, DEVICES_API };
	private static final String[] APPLICATION_API = { DEVICES_API, ACCOUNTS_API, ACTIONS_API };

	public static final String ROLE_ADMIN = "ROLE_ADMIN";
	public static final String ROLE_APPLICATION = "ROLE_APPLICATION";
	public static final String ROLE_DEVICE = "ROLE_DEVICE";


	@Autowired
	AtlasAuthenticationProvider atlasAuthenticationProvider;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(); // generates a 60char hash using random salt!
	}

	@Autowired
	@Override
	public void configure(AuthenticationManagerBuilder auth) {
		 auth.authenticationProvider(atlasAuthenticationProvider);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// Logging
		http.addFilterBefore(new LoggingFilter(), BasicAuthenticationFilter.class);

		// Exception handling
		http.addFilterAfter(new ExceptionHandlerFilter(), BasicAuthenticationFilter.class);

		// Authentication
		http.addFilterAfter(new AuthenticationFilter(authenticationManager()), ExceptionHandlerFilter.class);

		http.csrf().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.authorizeRequests()
				// Permit Swagger and Metrics APIs
				.regexMatchers(ATLAS_WHITELIST_REGEX).permitAll()
				.regexMatchers(SWAGGER_WHITELIST_REGEX).permitAll()

				// Permit User Authentication API
				.antMatchers(HttpMethod.POST, "/accounts").permitAll()
				.antMatchers(HttpMethod.GET, "/accounts/id").permitAll()

				// Authorize rest APIs
				.regexMatchers(HttpMethod.PUT, "/devices/.*").hasAnyRole("ADMIN", "APPLICATION", "DEVICE")
				.regexMatchers(HttpMethod.POST, "/events").hasAnyRole("ADMIN", "APPLICATION", "DEVICE")
				.antMatchers(APPLICATION_API).hasAnyRole("ADMIN", "APPLICATION")
				.antMatchers(ADMIN_API).hasRole("ADMIN")

				// Authenticate rest APIs
				.anyRequest().authenticated() // implicitly permit with .permitAll()
				.and()
				.httpBasic();
	}

	public static Collection<String> getPublicApis() {
		return Set.of(Arrays.asList(APPLICATION_API), Arrays.asList(DEVICE_API))
				.stream()
				.flatMap(Collection::stream)
				.map(api -> api.replace("/**", ".*"))
				.collect(Collectors.toList());
	}
}