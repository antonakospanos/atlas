package org.antonakospanos.iot.atlas.web.configuration;

import org.antonakospanos.iot.atlas.web.security.filters.AuthorizationFilter;
import org.antonakospanos.iot.atlas.web.security.filters.ExceptionHandlerFilter;
import org.antonakospanos.iot.atlas.web.security.filters.LoggingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

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

	private static final String[] ADMIN_API = new String[]{"/admin/**"};
	private static final String[] DEVICE_API = new String[]{ EVENTS_API, DEVICES_API };
	private static final String[] APPLICATION_API = new String[]{ DEVICES_API, ACCOUNTS_API, ACTIONS_API };

	private static final String ROLE_ADMIN = "ADMIN_USER";
	private static final String ROLE_DEVICE = "DEVICE_USER";
	private static final String ROLE_APPLICATION = "APPLICATION_USER";

	@Value("${application.roles.admin.username}")
	private String adminUsername;

	@Value("${application.roles.admin.password}")
	private String adminPassword;

	@Value("${application.roles.user.username}")
	private String applicationUsername;

	@Value("${application.roles.user.password}")
	private String applicationPassword;


	@Autowired
	UserDetailsService userDetailsService;

	@Bean
	public UserDetailsService userDetailsService() {
		return super.userDetailsService();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(); // generates a 60char hash using random salt!
	}

	@Autowired
	public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
		// auth.userDetailsService(userDetailsService);
		auth.authenticationProvider(authenticationProvider());
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.inMemoryAuthentication()
				.withUser(applicationUsername)
				.password(applicationPassword)
				.roles(ROLE_APPLICATION)
			.and()
				.withUser(adminUsername)
				.password(adminPassword)
				.roles(ROLE_ADMIN);
	}

//	@Override
//	public void configure(WebSecurity web) throws Exception {
//		web
//				.ignoring()
//				.antMatchers(ATLAS_WHITELIST_REGEX)
//				.antMatchers(SWAGGER_WHITELIST_REGEX)
//				.antMatchers(EVENTS_API)
//				.antMatchers(DEVICES_API)
//				.antMatchers(ACTIONS_API);
//	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// Logging
		http.addFilterBefore(new LoggingFilter(), BasicAuthenticationFilter.class);

		// Authentication
		http.authorizeRequests()
					.regexMatchers(ATLAS_WHITELIST_REGEX).permitAll()
					.regexMatchers(SWAGGER_WHITELIST_REGEX).permitAll()
					// .antMatchers(ADMIN_API).hasAnyAuthority(ROLE_ADMIN) // authenticated()
					// .antMatchers(DEVICE_API).hasAnyAuthority(ROLE_ADMIN, ROLE_DEVICE) // authenticated()
					// .antMatchers(APPLICATION_API).hasAnyAuthority(ROLE_ADMIN, ROLE_APPLICATION) // authenticated()

					// implicitly permit!
					.anyRequest().permitAll()
					// TODO: implicitly require authentication!
					// .anyRequest().authenticated()
				.and()
					.csrf().disable();

		// API Authorization
		http.addFilterAfter(new ExceptionHandlerFilter(), BasicAuthenticationFilter.class);
		http.addFilterAfter(new AuthorizationFilter(), ExceptionHandlerFilter.class);

		// API Authorization for anonymous!
		// AnonymousAuthenticationFilter anonymousAuthenticationFilter = new AnonymousAuthenticationFilter(UUID.randomUUID().toString());
		// anonymousAuthenticationFilter.setAuthenticationDetailsSource(new AuthenticationSource());
		// http.anonymous().authenticationFilter(anonymousAuthenticationFilter);
	}
}