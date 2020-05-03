package com.gil.arizon.juan.authorisation.dto;

import lombok.Data;

@Data
public class LoginResponseDto {
  private String accessToken;
  private String tokenType = "Bearer";
  private UserDetailsDto userDetails;

  public LoginResponseDto(String accessToken, UserDetailsDto userDetails) {
    this.accessToken = accessToken;
    this.userDetails= userDetails;
  }
}
