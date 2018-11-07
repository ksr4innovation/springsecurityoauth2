package com.snkit.springbootclientoauth2;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class SnkitCustomCodeToken extends AbstractAuthenticationToken {

	private final Object principal;
	private Object credentials;

	public SnkitCustomCodeToken(Object principal, Object credentials) {
		super(null);
		this.principal = principal;
		this.credentials = credentials;
		setAuthenticated(false);
	}

	public SnkitCustomCodeToken(Object principal, Object credentials,
			Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.principal = principal;
		this.credentials = credentials;
		super.setAuthenticated(true); // must use super, as we override
	}

	@Override
	public Object getCredentials() {

		return credentials;
	}

	@Override
	public Object getPrincipal() {

		return principal;
	}

}
