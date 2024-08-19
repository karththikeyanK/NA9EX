package com.express.user.service;

import com.express.user.dto.AddressRequest;
import com.express.user.dto.AddressResponse;
import com.express.user.dtoMaper.AddressDtoMapper;
import com.express.user.entity.Address;
import com.express.user.entity.Customer;
import com.express.user.exception.DataAccessException;
import com.express.user.repo.AddressRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final EntityManager entityManager;
    private final CustomerService customerService;

    public AddressResponse createAddress(AddressRequest addressRequest) {
        log.info("AddressService::createAddress()::Creating address started");
        Customer customer = ServiceUtil.validateEntity(customerService.existById(addressRequest.getCustomerId()),()-> entityManager.getReference(Customer.class, addressRequest.getCustomerId()), "Customer" ,addressRequest.getCustomerId());
        Address address = AddressDtoMapper.toAddress(addressRequest, customer);
        address = addressRepository.save(address);
        log.info("AddressService::createAddress()::Creating address completed");

        return AddressDtoMapper.toAddressResponse(address);
    }

    public AddressResponse getAddress(Long addressId) {
        log.info("AddressService::getAddress()::Fetching address started");
        Address address = addressRepository.findById(addressId).orElseThrow(() -> new DataAccessException("Address not found with id: " + addressId));
        log.info("AddressService::getAddress()::Fetching address completed");
        return AddressDtoMapper.toAddressResponse(address);
    }

    public Address getAddressEntity(Long addressId) {
        log.info("AddressService::AddressEntity()::Fetching address started");
        Address address = addressRepository.findById(addressId).orElseThrow(() -> new DataAccessException("Address not found with id: " + addressId));
        log.info("AddressService::AddressEntity()::Fetching address completed");
        return address;
    }

    public AddressResponse updateAddress(Long addressId, AddressRequest addressRequest) {
        log.info("AddressService::updateAddress()::Updating address started");
        if (!addressRepository.existsById(addressId)) {
            log.error("AddressService::updateAddress()::Address not found with id: {}", addressId);
            throw new DataAccessException("Address not found with id: " + addressId);
        }
        Customer customer = ServiceUtil.validateEntity(customerService.existById(addressRequest.getCustomerId()),()-> entityManager.getReference(Customer.class, addressRequest.getCustomerId()), "Customer" ,addressRequest.getCustomerId());
        Address address = AddressDtoMapper.toAddress(addressRequest, customer);
        address.setId(addressId);
        address = addressRepository.save(address);
        log.info("AddressService::updateAddress()::Updating address completed");
        return AddressDtoMapper.toAddressResponse(address);
    }

    public void deleteAddress(Long addressId) {
        log.info("AddressService::deleteAddress()::Deleting address started");
        addressRepository.deleteById(addressId);
        log.info("AddressService::deleteAddress()::Deleting address completed");
    }


    public AddressResponse findByAddress(String add){
        log.info("AddressService::findByAddress()::Fetching address started");
        Address address = addressRepository.findByAddress(add);
        if (address == null) {
            log.error("AddressService::findByAddress()::Address not found with address: {}", add);
            throw new DataAccessException("Address not found with address: " + add);
        }
        log.info("AddressService::findByAddress()::Fetching address completed");
        return AddressDtoMapper.toAddressResponse(address);
    }

    public boolean isExistById(Long id){
        log.info("AddressService::isExistBy()::Started");
        return addressRepository.existsById(id);
    }


    public boolean isAddressExist(String add){
        log.info("AddressService::isAddressExist()::Checking address started");
        return addressRepository.existsByAddress(add);
    }

    public boolean isAddressExistAndCustomer(String add, Long customerId){
        log.info("AddressService::isAddressExistAndCustomer()::Checking address started");
        Customer customer = ServiceUtil.validateEntity(customerService.existById(customerId),()-> entityManager.getReference(Customer.class, customerId), "Customer" ,customerId);
        return addressRepository.existsByAddressAndCustomer(add, customer);
    }

    public AddressResponse getAddressByAddressAndCustomer(String add, Long customerId){
        log.info("AddressService::getAddressByAddressAndCustomer()::Fetching address started");
        Customer customer = ServiceUtil.validateEntity(customerService.existById(customerId),()-> entityManager.getReference(Customer.class, customerId), "Customer" ,customerId);
        Address address = addressRepository.findByAddressAndCustomer(add, customer);
        if (address == null) {
            log.error("AddressService::getAddressByAddressAndCustomer()::Address not found with address: {}", add);
            throw new DataAccessException("Address not found with address: " + add);
        }
        log.info("AddressService::getAddressByAddressAndCustomer()::Fetching address completed");
        return AddressDtoMapper.toAddressResponse(address);
    }

    public List<AddressResponse> findAddsByCustomerID(Long id){
        log.info("AddressService::findAddsByCustomerID()::Fetching address started");
        Customer customer = ServiceUtil.validateEntity(customerService.existById(id),()-> entityManager.getReference(Customer.class, id), "Customer" ,id);
        List<Address> addressList = addressRepository.findByCustomer(customer);
        log.info("AddressService::findAddsByCustomerID()::Fetching address completed");
        return addressList.stream().map(AddressDtoMapper::toAddressResponse).collect(Collectors.toList());
    }

}
