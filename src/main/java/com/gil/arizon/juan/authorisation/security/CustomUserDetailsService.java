package com.gil.arizon.juan.authorisation.security;

import com.gil.arizon.juan.authorisation.domain.MyUser;
import com.gil.arizon.juan.authorisation.repository.MyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  @Autowired
  MyUserRepository userRepository;

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

  // This method is used by JWTAuthenticationFilter
  @Transactional
  public UserDetails loadUserById(Long id) {
    MyUser user = userRepository.findById(id).orElseThrow(
        () -> new UsernameNotFoundException("User not found with id : " + id)
    );

    return CustomUserDetails.create(user);
  }
}
