package com.snkit.springbootclientoauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.GenericFilterBean;

@Configuration
@EnableWebSecurity
@ImportResource({ "classpath:security-configuration.xml" })
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	@Qualifier("customAuthenticationProvider")
	AuthenticationProvider customAuthenticationProvider;

	@Autowired
	@Qualifier("customUsernamePasswordFilter")
	GenericFilterBean customUsernamePasswordFilter;

	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests().antMatchers("/api/rest/**").authenticated()
		.antMatchers("/nonprotectedurl").permitAll();

		http.addFilterBefore(customUsernamePasswordFilter, UsernamePasswordAuthenticationFilter.class);
		
	}

	@Override
	public void configure(AuthenticationManagerBuilder builder) throws Exception {
		builder.authenticationProvider(customAuthenticationProvider);

	}

}
