package com.express.user.auth;

import com.express.user.dto.AuthenticationRequest;
import com.express.user.dto.AuthenticationResponse;
import com.express.user.dto.UserRequest;
import com.express.user.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService authenticationService;

  @PostMapping("/register")
  public ResponseEntity<ApiResponse<AuthenticationResponse>> register(@RequestBody UserRequest request) {
     try{

       return ResponseEntity.ok().body(new ApiResponse<>(ApiResponse.SUCCESS, "User registered successfully", authenticationService.register(request)));
     } catch (Exception e) {
          return ResponseEntity.badRequest().body(new ApiResponse<AuthenticationResponse>(ApiResponse.ERROR, e.getMessage(), null));
     }
  }


  /*
      * This method is used to authenticate a user and return a token
   */

  @PostMapping("/authenticate")
  public ResponseEntity<ApiResponse<AuthenticationResponse>> authenticate(@RequestBody AuthenticationRequest request) {
      try{
        return ResponseEntity.ok().body(new ApiResponse<AuthenticationResponse>(ApiResponse.SUCCESS, "User authenticated successfully", authenticationService.authenticate(request)));
      } catch (Exception e) {
        return ResponseEntity.badRequest().body(new ApiResponse<AuthenticationResponse>(ApiResponse.ERROR, e.getMessage(), null));
      }
  }


}
