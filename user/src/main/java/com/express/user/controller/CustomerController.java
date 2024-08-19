package com.express.user.controller;


import com.express.user.dto.CustomerAndAddressResponse;
import com.express.user.dto.CustomerRequest;
import com.express.user.dto.CustomerResponse;
import com.express.user.facade.CustomerFacade;
import com.express.user.response.ApiResponse;
import com.express.user.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    private final CustomerFacade customerFacade;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<CustomerResponse>> create(@RequestBody CustomerRequest customerRequest){
        return ResponseEntity.ok(new ApiResponse<CustomerResponse>(ApiResponse.SUCCESS, "Ticket created successfully", customerService.create(customerRequest)));
    }

    @GetMapping("getAllDetails")
    public ResponseEntity<ApiResponse<List<CustomerAndAddressResponse>>> getAllCustomerAndAddress() {
        return ResponseEntity.ok(new ApiResponse<>(ApiResponse.SUCCESS, "Customer and address fetched successfully", customerFacade.getAllCustomerAndAddress()));
    }


}
