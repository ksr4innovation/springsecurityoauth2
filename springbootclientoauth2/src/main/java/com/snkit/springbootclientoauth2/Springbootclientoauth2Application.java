package com.snkit.springbootclientoauth2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
public class Springbootclientoauth2Application {

	public static void main(String[] args) {
		SpringApplication.run(Springbootclientoauth2Application.class, args);
	}
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	
	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}
}
