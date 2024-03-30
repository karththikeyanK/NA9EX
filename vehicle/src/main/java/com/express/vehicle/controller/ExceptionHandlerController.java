package com.express.vehicle.controller;

import com.express.user.response.ApiErrorResponse;
import com.express.user.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<ApiErrorResponse>> handleException(Exception e){
        return ResponseEntity.badRequest().body(new ApiResponse<ApiErrorResponse>(ApiResponse.ERROR, e.getMessage(), null));
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiResponse<ApiErrorResponse>> handleDataAccessException(DataAccessException e){
        return ResponseEntity.badRequest().body(new ApiResponse<ApiErrorResponse>(ApiResponse.ERROR, e.getMessage(), null));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponse<ApiErrorResponse>> handleValidationException(ValidationException e){
        return ResponseEntity.badRequest().body(new ApiResponse<ApiErrorResponse>(ApiResponse.ERROR, e.getMessage(), null));
    }


    @ExceptionHandler(RecordCreateException.class)
    public ResponseEntity<ApiResponse<ApiErrorResponse>> handleRecordCreateException(RecordCreateException e){
        return ResponseEntity.badRequest().body(new ApiResponse<ApiErrorResponse>(ApiResponse.ERROR, e.getMessage(), null));
    }


    @ExceptionHandler(DuplicationException.class)
    public ResponseEntity<ApiResponse<ApiErrorResponse>> handleRecordCreateException(DuplicationException e){
        return ResponseEntity.badRequest().body(new ApiResponse<ApiErrorResponse>(ApiResponse.ERROR, e.getMessage(), null));
    }

    @ExceptionHandler(GeneralBusinessException.class)
    public ResponseEntity<ApiResponse<ApiErrorResponse>> handleGeneralBusinessException(GeneralBusinessException e){
        return ResponseEntity.badRequest().body(new ApiResponse<ApiErrorResponse>(ApiResponse.ERROR, e.getMessage(), null));
    }


}
