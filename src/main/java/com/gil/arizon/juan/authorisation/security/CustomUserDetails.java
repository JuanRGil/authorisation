package com.gil.arizon.juan.authorisation.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gil.arizon.juan.authorisation.domain.MyUser;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class CustomUserDetails implements UserDetails {

  private Long id;

  private String name;

  private String surname;

  private String username;

  @JsonIgnore
  private String email;

  @JsonIgnore
  private String password;

  private Collection<? extends GrantedAuthority> authorities;

  @JsonIgnore
  private Boolean isEnabled;


  public static CustomUserDetails create(MyUser user) {
    List<GrantedAuthority> authorities = null;
    if (user.getRole() != null){
      authorities = Arrays.asList(
          new SimpleGrantedAuthority(user.getRole().getRoleName()));
    }
    return new CustomUserDetails(
        user.getId(),
        user.getName(),
        user.getSurname(),
        user.getUserName(),
        user.getEmail(),
        user.getPassword(),
        authorities,
        user.getIsEnabled()
    );
  }

  @Override
  public boolean isEnabled() {
    return this.isEnabled;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

}
