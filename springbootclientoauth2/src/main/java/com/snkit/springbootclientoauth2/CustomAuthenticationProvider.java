package com.snkit.springbootclientoauth2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomAuthenticationProvider implements AuthenticationProvider {

	
	UserDetailsService userDetailsService;
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	ObjectMapper objectMapper;

	public CustomAuthenticationProvider() {

	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		SnkitCustomCodeToken auth = (SnkitCustomCodeToken) authentication;
		
		String username=null;
		
		if (auth.getPrincipal() != null) {
			CustomTokenVO customTokenVO = null;
			customTokenVO = new CustomTokenVO();
			System.out.println("   From code to get token oAuth2 flow");
			
			String credentials = "snkitclient:snkitclientdemo";
			
			String encodedCredentails = new String(Base64.encodeBase64(credentials.getBytes()));
			
			HttpHeaders headers = new HttpHeaders();
			headers.add( "Authorization", "Basic "+encodedCredentails);
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			
			HttpEntity<String> entity = new HttpEntity<String>(headers);
		String url = 	"http://localhost:8080/oauth/token?code="+auth.getPrincipal() ;
		url= url+"&grant_type=authorization_code&redirect_uri=http://localhost:8070/api/rest/hi";
	
			
		ResponseEntity<String> response = 	restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
		
		System.out.println("   passing code and getting token ==== "+response.getBody());
		
		try {
			customTokenVO = objectMapper.readValue(response.getBody(), CustomTokenVO.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		username = customTokenVO.getAccess_token();
		}else {
		
		
		System.out.println("   From token else   oAuth2 flow");
		//String credentials = "snkitclient:snkitclientdemo";
		
		//String encodedCredentails = new String(Base64.encodeBase64(credentials.getBytes()));
		
		HttpHeaders headers = new HttpHeaders();
		headers.add( "Authorization", "Bearer "+auth.getCredentials());
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		String checktoken = 	"http://localhost:8080/oauth/check_token?token="+auth.getCredentials();

			
		ResponseEntity<String> checktokenResp = 	restTemplate.exchange(checktoken, HttpMethod.GET, entity, String.class);
		
		System.out.println("   passing token  and validating token ==== "+checktokenResp.getBody());
		
		username = auth.getCredentials().toString();
		}
	return new SnkitCustomCodeToken(username,username,new ArrayList<>());
	}

	public boolean supports(Class<?> authentication) {
		return (SnkitCustomCodeToken.class.isAssignableFrom(authentication));
	}

	public UserDetailsService getUserDetailsService() {
		return userDetailsService;
	}

	public void setUserDetailsService(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	
	
}
