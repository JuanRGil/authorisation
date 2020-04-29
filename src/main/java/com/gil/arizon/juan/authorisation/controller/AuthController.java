package com.gil.arizon.juan.authorisation.controller;

import com.gil.arizon.juan.authorisation.domain.MyUser;
import com.gil.arizon.juan.authorisation.dto.LoginRequestDto;
import com.gil.arizon.juan.authorisation.dto.LoginResponseDto;
import com.gil.arizon.juan.authorisation.dto.SignUpDto;
import com.gil.arizon.juan.authorisation.mapper.MyUserMapper;
import com.gil.arizon.juan.authorisation.repository.MyUserRepository;
import com.gil.arizon.juan.authorisation.security.JwtTokenProvider;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  MyUserRepository userRepository;

  @Autowired
  JwtTokenProvider tokenProvider;

  @Autowired
  MyUserMapper myUserMapper;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@RequestBody LoginRequestDto loginRequest) {


    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            loginRequest.getUsernameOrEmail(),
            loginRequest.getPassword()
        )
    );

    SecurityContextHolder.getContext().setAuthentication(authentication);

    String jwt = tokenProvider.generateToken(authentication);
    return ResponseEntity.ok(new LoginResponseDto(jwt));
  }

  @PostMapping("/signup")
  public ResponseEntity<String> registerUser(@RequestBody SignUpDto signUpRequest) {
    Optional<MyUser> found = userRepository.findByUserNameOrEmail(signUpRequest.getUserName(), signUpRequest.getEmail());
    if(found.isPresent()) {
      return new ResponseEntity("Username or email already in use!",
          HttpStatus.BAD_REQUEST);
    }
    // Creating user's account
    MyUser user = myUserMapper.from(signUpRequest);
    userRepository.save(user);
    return ResponseEntity.ok("User registered successfully");
  }
}
