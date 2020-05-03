package com.gil.arizon.juan.authorisation.mapper;

import com.gil.arizon.juan.authorisation.domain.MyUser;
import com.gil.arizon.juan.authorisation.dto.SignUpDto;
import com.gil.arizon.juan.authorisation.security.CustomUserDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;


@Mapper( componentModel = "spring")
public abstract class MyUserMapper {

  @Autowired
  protected PasswordEncoder passwordEncoder;

  @Mappings({
      @Mapping(source = "userName", target = "userName"),
      @Mapping(source = "password", target = "password", qualifiedByName = "encodePassword"),
      @Mapping(source = "name", target = "name"),
      @Mapping(source = "surname", target = "surname"),
      @Mapping(source = "email", target = "email")
  })
  public abstract MyUser from(SignUpDto user);

  @Named("encodePassword")
  public String encodePassword(String passToEncode) {

    return passToEncode!= null ? passwordEncoder.encode(passToEncode): null;
  }
}
