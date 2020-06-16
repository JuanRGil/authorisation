package com.gil.arizon.juan.authorisation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

@SpringBootApplication
@EnableAuthorizationServer
//https://codeaches.com/spring-cloud-security/oauth2-authorization-jwt
public class AuthorisationApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthorisationApplication.class, args);
	}

}
