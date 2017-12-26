package org.antonakospanos.iot.atlas.web.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private static final String[] SWAGGER_WHITELIST = {"/swagger-resources/**", "/swagger-ui.html", "docs/api.html",
			"/v2/api-docs/**", "/docs/**", "/webjars/**", "/configuration/ui", "/configuration/security"};

	private static final String[] ATLAS_WHITELIST = new String[]{"/", "/version", "/health", "/ehcache/**", "/metrics/**"};

	private static final String[] ALERTS_API = new String[]{"/alerts"};
	private static final String[] ACTIONS_API = new String[]{"/actions"};
	private static final String[] ADMIN_API = new String[]{"/admin"};

	private static final String ROLE_APPLICATION = "APPLICATION_USER";
	private static final String ROLE_ADMIN = "ADMIN_USER";

	@Value("${application.roles.admin.username}")
	private String adminUsername;

	@Value("${application.roles.admin.password}")
	private String adminPassword;

	@Value("${application.roles.user.username}")
	private String applicationUsername;

	@Value("${application.roles.user.password}")
	private String applicationPassword;

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
//				.antMatchers(ATLAS_WHITELIST)
//				.antMatchers(SWAGGER_WHITELIST)
//				.antMatchers(EVENTS_API)
//				.antMatchers(DEVICES_API)
//				.antMatchers(ACTIONS_API);
//	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests()
					.antMatchers(ADMIN_API).hasRole(ROLE_ADMIN) // authenticated()
					.anyRequest().permitAll() // implicitly permit!
				.and()
					.csrf().disable();
	}
}