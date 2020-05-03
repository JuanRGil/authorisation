package com.gil.arizon.juan.authorisation.security;

import com.gil.arizon.juan.authorisation.domain.MyUser;
import com.gil.arizon.juan.authorisation.dto.SignUpDto;
import com.gil.arizon.juan.authorisation.mapper.MyUserMapper;
import com.gil.arizon.juan.authorisation.repository.MyUserRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  @Autowired
  MyUserRepository userRepository;

  @Autowired
  MyUserMapper myUserMapper;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String usernameOrEmail)
      throws UsernameNotFoundException {
    // Let people login with either userName or email
    MyUser user = userRepository.findByUserNameOrEmail(usernameOrEmail, usernameOrEmail)
        .orElseThrow(() ->
            new UsernameNotFoundException("User not found with userName or email : " + usernameOrEmail)
        );

    return CustomUserDetails.create(user);
  }

  public UserDetails save(SignUpDto signUpRequest){
    Optional<MyUser> found = userRepository.findByUserNameOrEmail(signUpRequest.getUserName(), signUpRequest.getEmail());
    if(found.isPresent()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username or email already in use!");
    }
    // Creating user's account
    MyUser user = myUserMapper.from(signUpRequest);
    MyUser saved = userRepository.save(user);
    return CustomUserDetails.create(saved);
  }

  // This method is used by JWTAuthenticationFilter
  @Transactional
  public UserDetails loadUserById(Long id) {
    MyUser user = userRepository.findById(id).orElseThrow(
        () -> new UsernameNotFoundException("User not found with id : " + id)
    );

    return CustomUserDetails.create(user);
  }
}
