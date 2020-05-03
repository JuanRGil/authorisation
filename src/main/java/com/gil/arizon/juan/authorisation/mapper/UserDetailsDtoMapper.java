package com.gil.arizon.juan.authorisation.mapper;

import com.gil.arizon.juan.authorisation.dto.UserDetailsDto;
import com.gil.arizon.juan.authorisation.security.CustomUserDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper( componentModel = "spring")
public abstract class UserDetailsDtoMapper {

  @Mappings({
      @Mapping(source = "username", target = "username"),
      @Mapping(source = "name", target = "name"),
      @Mapping(source = "surname", target = "surname"),
      @Mapping(source = "email", target = "email")
  })
  public abstract UserDetailsDto from(CustomUserDetails user);

}
