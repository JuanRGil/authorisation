package com.gil.arizon.juan.authorisation.dto;

import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginRequestDto {

  @NotBlank
  private String usernameOrEmail;

  @NotBlank
  private String password;
}
