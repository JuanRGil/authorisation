package com.gil.arizon.juan.authorisation.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpDto {

  @NotBlank
  @Size(min = 3, max = 15)
  private String userName;

  @NotBlank
  @Size(max = 40)
  @Email
  private String email;

  @NotBlank
  @Size(min = 6, max = 20)
  private String password;

  @NotBlank
  @Size(min = 4, max = 40)
  private String name;

  @NotBlank
  @Size(min = 6, max = 20)
  private String surname;

}
