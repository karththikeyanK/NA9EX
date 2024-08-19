package com.express.user.facade;

import com.express.user.dto.CustomerAndAddressResponse;
import com.express.user.dto.CustomerResponse;
import com.express.user.service.AddressService;
import com.express.user.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerFacade {

    private final CustomerService customerService;
    private final AddressService addressService;

    public List<CustomerAndAddressResponse> getAllCustomerAndAddress() {
        log.info("CustomerFacade::getAllCustomerAndAddress()::Fetching all customer and address started");
        List<CustomerResponse> customerResponseList = customerService.getAllCustomers();
        List<CustomerAndAddressResponse> customerAndAddressResponseList = customerResponseList.stream()
                .map(customerResponse -> {
                    CustomerAndAddressResponse customerAndAddressResponse = new CustomerAndAddressResponse();
                    customerAndAddressResponse.setCustomerResponse(customerResponse);
                    customerAndAddressResponse.setAddressResponseList(addressService.findAddsByCustomerID(customerResponse.getId()));
                    return customerAndAddressResponse;
                }).collect(Collectors.toList());
        log.info("CustomerFacade::getAllCustomerAndAddress()::Fetching all customer and address completed");
        return customerAndAddressResponseList;
    }
}
