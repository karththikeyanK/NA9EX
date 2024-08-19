package com.express.user.dtoMaper;

import com.express.user.dto.AddressRequest;
import com.express.user.dto.AddressResponse;
import com.express.user.entity.Address;
import com.express.user.entity.Customer;

public class AddressDtoMapper {

    public static Address toAddress(AddressRequest addressRequest, Customer customer) {
        return Address.builder()
                .address(addressRequest.getAddress())
                .customer(customer)
                .build();
    }

    public static AddressResponse toAddressResponse(Address address) {
        return AddressResponse.builder()
                .id(address.getId())
                .customerId(address.getCustomer().getId())
                .address(address.getAddress())
                .build();
    }

    public static AddressRequest toAddressRequest(String add,Long customerId){
        return AddressRequest.builder()
                .address(add)
                .customerId(customerId)
                .build();
    }
}
