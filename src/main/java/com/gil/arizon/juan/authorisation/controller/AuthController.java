package com.gil.arizon.juan.authorisation.controller;

import com.gil.arizon.juan.authorisation.domain.MyUser;
import com.gil.arizon.juan.authorisation.dto.LoginRequestDto;
import com.gil.arizon.juan.authorisation.dto.LoginResponseDto;
import com.gil.arizon.juan.authorisation.dto.SignUpDto;
import com.gil.arizon.juan.authorisation.dto.UserDetailsDto;
import com.gil.arizon.juan.authorisation.mapper.MyUserMapper;
import com.gil.arizon.juan.authorisation.mapper.UserDetailsDtoMapper;
import com.gil.arizon.juan.authorisation.repository.MyUserRepository;
import com.gil.arizon.juan.authorisation.security.CustomUserDetails;
import com.gil.arizon.juan.authorisation.security.CustomUserDetailsService;
import com.gil.arizon.juan.authorisation.security.JwtTokenProvider;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  MyUserRepository userRepository;

  @Autowired
  JwtTokenProvider tokenProvider;

  @Autowired
  UserDetailsDtoMapper userDetailsDtoMapper;

  @Autowired
  CustomUserDetailsService customUserDetailsService;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@RequestBody LoginRequestDto loginRequest) {
    //TODO mirar esto
    //https://codeaches.com/spring-cloud-security/oauth2-authorization-jwt
    //https://docs.spring.io/spring-security-oauth2-boot/docs/2.0.0.RC2/reference/htmlsingle/

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            loginRequest.getUsername(),
            loginRequest.getPassword()
        )
    );

    SecurityContextHolder.getContext().setAuthentication(authentication);
    CustomUserDetails principal = (CustomUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    UserDetailsDto userDetails = userDetailsDtoMapper.from(principal);
    String jwt = tokenProvider.generateToken(authentication);
    return ResponseEntity.ok(new LoginResponseDto(jwt, userDetails));
  }

  @PostMapping("/signup")
  public ResponseEntity<String> registerUser(@RequestBody SignUpDto signUpRequest) {
    customUserDetailsService.save(signUpRequest);
    return ResponseEntity.ok("User registered successfully");
  }

  @GetMapping("/user")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<?> getCurrentUser(Authentication authObject) {
    log.info("obteniendo información del usuario: {}", authObject.getPrincipal());
    return (authObject.getPrincipal() != null
        ? ResponseEntity.ok((CustomUserDetails)authObject.getPrincipal())
        : ResponseEntity.badRequest().body("no hay ningún usuario autenticado"));
  }
}
