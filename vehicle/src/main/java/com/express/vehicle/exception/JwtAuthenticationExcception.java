package com.express.vehicle.exception;

public class JwtAuthenticationExcception extends RuntimeException{

      public JwtAuthenticationExcception(String message) {
         super(message);
      }
}
