package com.express.user.controller;

import com.express.user.exception.*;
import com.express.user.response.ApiErrorResponse;
import com.express.user.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleException(Exception e){
        return ResponseEntity.badRequest().body(new ApiErrorResponse(ApiErrorResponse.ERROR, e.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiErrorResponse> handleRuntimeException(RuntimeException e){
        return ResponseEntity.badRequest().body(new ApiErrorResponse(ApiErrorResponse.ERROR, e.getMessage()));
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiResponse<ApiErrorResponse>> handleDataAccessException(DataAccessException e){
        return ResponseEntity.badRequest().body(new ApiResponse<ApiErrorResponse>(ApiResponse.ERROR, e.getMessage(), null));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationException(ValidationException e){
        return ResponseEntity.badRequest().body(new ApiErrorResponse(ApiErrorResponse.ERROR, e.getMessage()));
    }


    @ExceptionHandler(RecordCreateException.class)
    public ResponseEntity<ApiErrorResponse> handleRecordCreateException(RecordCreateException e){
        return ResponseEntity.badRequest().body(new ApiErrorResponse(ApiResponse.ERROR, e.getMessage()));
    }


    @ExceptionHandler(DuplicationException.class)
    public ResponseEntity<ApiErrorResponse> handleRecordCreateException(DuplicationException e){
        return ResponseEntity.badRequest().body(new ApiErrorResponse(ApiErrorResponse.ERROR, e.getMessage()));
    }

    @ExceptionHandler(GeneralBusinessException.class)
    public ResponseEntity<ApiErrorResponse> handleGeneralBusinessException(GeneralBusinessException e){
        return ResponseEntity.badRequest().body(new ApiErrorResponse(ApiResponse.ERROR, e.getMessage()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFoundException(ResourceNotFoundException e){
        return ResponseEntity.badRequest().body(new ApiErrorResponse(ApiResponse.ERROR, e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgumentException(IllegalArgumentException e){
        return ResponseEntity.badRequest().body(new ApiErrorResponse(ApiResponse.ERROR, e.getMessage()));
    }


}
