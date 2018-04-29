package org.antonakospanos.iot.atlas.web.configuration;

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

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private static final String[] SWAGGER_WHITELIST = {"/swagger-resources/**", "/swagger-ui.html", "docs/api.html",
			"/v2/api-docs/**", "/docs/**", "/webjars/**", "/configuration/ui", "/configuration/security"};

	private static final String[] ATLAS_WHITELIST = new String[]{"/", "/version", "/health", "/ehcache/**", "/metrics/**"};

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
//				.antMatchers(ATLAS_WHITELIST)
//				.antMatchers(SWAGGER_WHITELIST)
//				.antMatchers(EVENTS_API)
//				.antMatchers(DEVICES_API)
//				.antMatchers(ACTIONS_API);
//	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
//		http.authorizeRequests()
//				.antMatchers("/", "/home").permitAll()
//				.antMatchers(ADMIN_API).access("hasRole('"+ROLE_ADMIN+"')")
//				.antMatchers(DEVICE_API).access("hasRole('"+ROLE_ADMIN+"') and hasRole('"+ROLE_DEVICE+"')")
//				.antMatchers(APPLICATION_API).access("hasRole('"+ROLE_ADMIN+"') and hasRole('"+ROLE_APPLICATION+"')")
//			.and().formLogin().loginPage("/login")
//				.usernameParameter("username").passwordParameter("password")
//			.and().csrf()
//			.and().exceptionHandling().accessDeniedPage("/Access_Denied");

		http.authorizeRequests()
					.antMatchers(ADMIN_API).hasRole(ROLE_ADMIN) // authenticated()
					.anyRequest().permitAll() // implicitly permit!
				.and()
					.csrf().disable();
	}
}