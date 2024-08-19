package com.express.user.controller;

import com.express.user.dto.UserRequest;
import com.express.user.dto.UserResponse;
import com.express.user.response.ApiResponse;
import com.express.user.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UsersService userService;

    @GetMapping("/test")
    public String test(){
        return "Hello World";
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@RequestBody UserRequest userRequest){
        return ResponseEntity.ok(new ApiResponse<UserResponse>(ApiResponse.SUCCESS, "User created successfully", userService.createUser(userRequest)));
    }

    @GetMapping("/getAll/{category}")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers(@PathVariable String category){
        return ResponseEntity.ok(new ApiResponse<List<UserResponse>>(ApiResponse.SUCCESS, "User fetched successfully", userService.getAllUsers(category)));
    }



}
