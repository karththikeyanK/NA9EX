package com.express.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerAndAddressResponse {
    private CustomerResponse customerResponse;
    private List<AddressResponse> addressResponseList;
}
