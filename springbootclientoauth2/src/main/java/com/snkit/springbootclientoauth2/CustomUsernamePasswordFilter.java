package com.snkit.springbootclientoauth2;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.web.filter.GenericFilterBean;

public class CustomUsernamePasswordFilter extends GenericFilterBean {

	public CustomUsernamePasswordFilter() {

	}

	private AuthenticationManager authenticationManager;

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		final HttpServletRequest request = (HttpServletRequest) req;

		final HttpServletResponse response = (HttpServletResponse) res;

		String url = request.getRequestURI();
		
		HttpSession session = request.getSession(false);
		


		if (url.contains("/api/rest/")) {

			String token = request.getHeader("Authorization");
			
			
			
			if (token == null && session != null) {
				token = (String)session.getAttribute("Authorization");
			}

			String code = request.getParameter("code");

			if (token == null && code == null) {
				response.sendRedirect(
						"http://localhost:8080/oauth/authorize?client_id=snkitclient&client_secret=snkitclientdemo"
								+ "&redirect_uri=http://localhost:8070/api/rest/hi&response_type=code");
				
				
				return;

			}
			try {
				SnkitCustomCodeToken customToken = null;
				if (code != null) {
					customToken = new SnkitCustomCodeToken(code, null);

				}

				if (token != null) {
					customToken = new SnkitCustomCodeToken(null, token);
				}

				Authentication auth = this.getAuthenticationManager().authenticate(customToken);
				
				SecurityContextHolder.getContext().setAuthentication(auth);
				
				 session = request.getSession(true);
				 
				  session.setAttribute("Authorization", SecurityContextHolder.getContext().getAuthentication().getName());
				  
				 
				  if (!auth.isAuthenticated()) {
					  return;
				  }

			} catch (Exception e) {

				e.printStackTrace();
			}
			
			

		}

		chain.doFilter(req, res);

	}

	private String[] extractAndDecodeHeader(String header, HttpServletRequest request) throws IOException {

		byte[] base64Token = header.substring(6).getBytes("UTF-8");
		byte[] decoded;
		try {
			decoded = Base64.decode(base64Token);
		} catch (IllegalArgumentException e) {
			throw new BadCredentialsException("Failed to decode basic authentication token");
		}
		String token = new String(decoded, "UTF-8");
		int delim = token.indexOf(":");

		if (delim == -1) {
			throw new BadCredentialsException("Invalid basic authentication token");
		}
		return new String[] { token.substring(0, delim), token.substring(delim + 1) };
	}

	public AuthenticationManager getAuthenticationManager() {
		return authenticationManager;
	}

	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

}
