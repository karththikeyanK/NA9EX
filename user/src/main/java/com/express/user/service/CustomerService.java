package com.express.user.service;

import com.express.user.dto.CustomerRequest;
import com.express.user.dto.CustomerResponse;
import com.express.user.dtoMaper.CustomerDtoMapper;
import com.express.user.entity.Customer;
import com.express.user.exception.DataAccessException;
import com.express.user.exception.ResourceNotFoundException;
import com.express.user.repo.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerResponse create(CustomerRequest customerRequest) {
       log.info("CustomerService::create()::Creating customer started");
       if (customerRepository.existsByMobile(customerRequest.getMobile())) {
           log.error("CustomerService::create()::Mobile Number already exists with mobile number: {}", customerRequest.getMobile());
           throw new RuntimeException("Mobile Number already exists with mobile number: " + customerRequest.getMobile());
       }
       Customer customer = CustomerDtoMapper.toCustomer(customerRequest);
       customer = customerRepository.save(customer);
       log.info("CustomerService::create()::Creating customer completed");
       return CustomerDtoMapper.toCustomerResponse(customer);
    }


    public CustomerResponse update(Long customerId, CustomerRequest customerRequest) {
        log.info("CustomerService::update()::Updating customer started");
        if (!customerRepository.existsById(customerId)) {
            log.error("CustomerService::update()::Customer not found with id: {}", customerId);
            throw new DataAccessException("Customer not found with id: " + customerId);
        }
        Customer customer = CustomerDtoMapper.toCustomer(customerRequest);
        customer.setId(customerId);
        customer = customerRepository.save(customer);
        log.info("CustomerService::update()::Updating customer completed");
        return CustomerDtoMapper.toCustomerResponse(customer);
    }

    public CustomerResponse getCustomer(Long customerId) {
        log.info("CustomerService::getCustomer()::Fetching customer started");
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new DataAccessException("Customer not found with id: " + customerId));
        log.info("CustomerService::getCustomer()::Fetching customer completed");
        return CustomerDtoMapper.toCustomerResponse(customer);
    }

    public CustomerResponse findCustomerResponseByMobile(String mobile) {
        log.info("CustomerService::findByMobile()::Fetching customer started");
        Customer customer = customerRepository.findByMobile(mobile);
        if (customer == null) {
            log.error("CustomerService::findByMobile()::Customer not found with mobile number: {}", mobile);
            throw new ResourceNotFoundException("Customer not found with mobile number: " + mobile);
        }
        log.info("CustomerService::findByMobile()::Fetching customer completed");
        return CustomerDtoMapper.toCustomerResponse(customer);
    }

    public Customer findCustomerEntityByMobile(String mobile) {
        log.info("CustomerService::findCustomerByMobile()::Fetching customer started");
        Customer customer = customerRepository.findByMobile(mobile);
        if (customer == null) {
            log.debug("CustomerService::findCustomerByMobile()::Customer not found with mobile number: {}", mobile);
            throw new ResourceNotFoundException("Customer not found with mobile number: " + mobile);
        }
        log.info("CustomerService::findCustomerByMobile()::Fetching customer completed");
        return customer;
    }


    public Customer getCustomerEntity(Long customerId) {
        log.info("CustomerService::getCustomerEntity()::Fetching customer started");
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));
        log.info("CustomerService::getCustomerEntity()::Fetching customer completed");
        return customer;
    }

    public List<CustomerResponse> getAllCustomers() {
        log.info("CustomerService::getAllCustomers()::Fetching all customers started");
        List<Customer> customers = customerRepository.findAll();
        if (customers.isEmpty()) {
            log.error("CustomerService::getAllCustomers()::No customers found");
            throw new ResourceNotFoundException("No customers found");
        }
        log.info("CustomerService::getAllCustomers()::Fetching all customers completed");
        return customers.stream().map(CustomerDtoMapper::toCustomerResponse).toList();
    }

    public Customer getEntityById(Long customerId) {
        log.info("CustomerService::getEntityById()::started");
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));
        log.info("CustomerService::getEntityById()::completed");
        return customer;
    }


    public boolean existById(Long customerId) {
        log.info("CustomerService::existById()::started");
        boolean isExist = customerRepository.existsById(customerId);
        log.info("CustomerService::existById()::completed");
        return isExist;
    }

    public boolean existByMobile(String mobile) {
        log.info("CustomerService::existByMobile()::started");
        boolean isExist = customerRepository.existsByMobile(mobile);
        log.info("CustomerService::existByMobile()::completed");
        return isExist;
    }
}
