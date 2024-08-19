package com.express.user.dtoMaper;

import com.express.user.dto.CustomerRequest;
import com.express.user.dto.CustomerResponse;
import com.express.user.entity.Customer;

import java.util.List;

public class CustomerDtoMapper {

    public static Customer toCustomer(CustomerRequest customerRequest) {
        return Customer.builder()
                .name(customerRequest.getName())
                .mobile(customerRequest.getMobile())
                .build();
    }

    public static CustomerResponse toCustomerResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .mobile(customer.getMobile())
                .build();
    }

    public static CustomerRequest toRequest(CustomerResponse customerResponse){
        return CustomerRequest.builder()
                .name(customerResponse.getName())
                .mobile(customerResponse.getMobile())
                .build();

    }

    public static CustomerResponse mapToCustomerResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .mobile(customer.getMobile())
                .build();
    }
}
