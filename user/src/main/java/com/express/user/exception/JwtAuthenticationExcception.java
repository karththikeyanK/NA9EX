package com.express.user.exception;

public class JwtAuthenticationExcception extends RuntimeException{

      public JwtAuthenticationExcception(String message) {
         super(message);
      }
}
