package com.gil.arizon.juan.authorisation.security;

import com.gil.arizon.juan.authorisation.dto.SignUpDto;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import java.io.IOException;
import java.util.Collections;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  public static final String AUTHORIZATION = "Authorization";
  public static final String AUTHORIZATION_PROVIDER = "AuthorizationProvider";
  public static final String GOOGLE_ID_TOKEN = "GoogleIdToken";
  public static final String TOKEN_TYPE = "Bearer";
  public static final String SEPARATOR = " ";

  @Autowired
  private JwtTokenProvider tokenProvider;

  @Autowired
  private CustomUserDetailsService customUserDetailsService;

  @Value("${app.google.clientid}")
  private String CLIENT_ID;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    try {
      String jwt = getJwtFromRequest(request);
      String authProvider = getAuthProvider(request);

      if("Google".equals(authProvider)){
        checkGoogleIdToken(request);
      } else {
        checkTicketUpToken(request, jwt);

      }
    } catch (Exception ex) {
      log.error("Could not set user authentication in security context", ex);
    }

    filterChain.doFilter(request, response);
  }

  private void checkTicketUpToken(HttpServletRequest request, String jwt) {
    if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
      Long userId = tokenProvider.getUserIdFromJWT(jwt);

      UserDetails userDetails = customUserDetailsService.loadUserById(userId);
      UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
      authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

      SecurityContextHolder.getContext().setAuthentication(authentication);
    }
  }

  private void checkGoogleIdToken(HttpServletRequest request) throws Exception {
    String idTokenValue = getIdToken(request);
    GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance())
        .setAudience(Collections.singletonList(CLIENT_ID))
        .build();

    GoogleIdToken idToken = verifier.verify(idTokenValue);
    if (idToken != null) {
      GoogleIdToken.Payload payload = idToken.getPayload();

      // Print user identifier
      String userId = payload.getSubject();
      System.out.println("User ID: " + userId);

      SignUpDto signUpGoogleUser = new SignUpDto();
      signUpGoogleUser.setEmail(payload.getEmail());
      signUpGoogleUser.setName((String)payload.get("name"));
      signUpGoogleUser.setSurname((String)payload.get("family_name"));
      signUpGoogleUser.setUserName(payload.getEmail());
      UserDetails userSaved = null;
      try {
        userSaved = customUserDetailsService.loadUserByUsername(payload.getEmail());
      } catch (UsernameNotFoundException userNotFound){
        userSaved = customUserDetailsService.save(signUpGoogleUser);
      }
      UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userSaved, null, userSaved.getAuthorities());
      authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
      SecurityContextHolder.getContext().setAuthentication(authentication);
    } else {
      throw new Exception("Can not autheticate througt google");
    }
  }

  private String getIdToken(HttpServletRequest request) {
    return request.getHeader(GOOGLE_ID_TOKEN);
  }

  private String getAuthProvider(HttpServletRequest request) {
    return request.getHeader(AUTHORIZATION_PROVIDER);

  }

  // it returns the token without token type ("Bearer\s")
  private String getJwtFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader(AUTHORIZATION);
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_TYPE.concat(SEPARATOR))) {
      return bearerToken.substring((TOKEN_TYPE.concat(SEPARATOR)).length());
    }
    return null;
  }
}
