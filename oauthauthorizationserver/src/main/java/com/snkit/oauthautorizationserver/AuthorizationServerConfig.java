package com.snkit.oauthautorizationserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenStoreUserApprovalHandler;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	static final String CLIEN_ID = "snkitclient";
	static final String CLIENT_SECRET = "snkitclientdemo";
	static final String GRANT_TYPE = "password";
	static final String AUTHORIZATION_CODE = "authorization_code";
	static final String REFRESH_TOKEN = "refresh_token";
	static final String IMPLICIT = "implicit";
	static final String SCOPE_READ = "read";
	static final String SCOPE_WRITE = "write";
	static final String TRUST = "trust";
	static final String profile = "profile";
	static final String openid="openid";
	static final int ACCESS_TOKEN_VALIDITY_SECONDS = 60 * 60 * 60;
	static final int FREFRESH_TOKEN_VALIDITY_SECONDS = 60 * 60 * 60;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
    private ClientDetailsService clientDetailsService;
	

	@Override
	public void configure(AuthorizationServerSecurityConfigurer authServer) throws Exception {
		

		authServer.checkTokenAccess("permitAll()");
		
		
		
		
		
	}

	@Bean
	public TokenStore tokenStore() {
		  return new InMemoryTokenStore();
	}
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

		clients.inMemory().withClient(CLIEN_ID).secret("{noop}"+CLIENT_SECRET)
				.authorizedGrantTypes(AUTHORIZATION_CODE, IMPLICIT, REFRESH_TOKEN,"client_credentials", "password")
				.scopes(SCOPE_READ, SCOPE_WRITE, TRUST).accessTokenValiditySeconds(ACCESS_TOKEN_VALIDITY_SECONDS)
				.refreshTokenValiditySeconds(FREFRESH_TOKEN_VALIDITY_SECONDS);
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		
		endpoints
		.authenticationManager(authenticationManager);
		endpoints.tokenStore(tokenStore());
	}

	  
	    @Bean
	    @Autowired
	    public TokenStoreUserApprovalHandler userApprovalHandler(TokenStore tokenStore){
	        TokenStoreUserApprovalHandler handler = new TokenStoreUserApprovalHandler();
	        handler.setTokenStore(tokenStore);
	        handler.setRequestFactory(new DefaultOAuth2RequestFactory(clientDetailsService));
	        handler.setClientDetailsService(clientDetailsService);
	        return handler;
	    }
	     
	    @Bean
	    @Autowired
	    public ApprovalStore approvalStore(TokenStore tokenStore) throws Exception {
	        TokenApprovalStore store = new TokenApprovalStore();
	        store.setTokenStore(tokenStore);
	        return store;
	    }
}
