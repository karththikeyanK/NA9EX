package com.express.user.auth;


import com.express.user.config.JwtService;
import com.express.user.dto.AuthenticationRequest;
import com.express.user.dto.AuthenticationResponse;
import com.express.user.dto.UserRequest;
import com.express.user.entity.User;
import com.express.user.exception.JwtAuthenticationExcception;
import com.express.user.repo.UserRepository;
import com.express.user.service.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
  private final UserRepository repository;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final UsersService usersService;

  public AuthenticationResponse register(UserRequest userRequest) {
      try{
        log.info("AuthenticationService::Registering user started");
        User user = usersService.Register(userRequest);
        log.info("AuthenticationService::Registering user completed");
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
      }catch (Exception e){
        log.error("AuthenticationService::Registering user failed");
        throw e;
      }
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
      log.info("AuthenticationService::Authenticating user started");
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              request.getEmail(),
              request.getPassword()
          )
      );
      var user = repository.findByEmail(request.getEmail()).orElseThrow(()->new JwtAuthenticationExcception("User not found with email "+request.getEmail()));
      var jwtToken = jwtService.generateToken(user);
        log.info("AuthenticationService::Authenticating user completed");
      return AuthenticationResponse.builder()
          .token(jwtToken)
          .build();
    }
}
