package com.express.user.repo;

import com.express.user.entity.Address;
import com.express.user.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    Address findByAddress(String add);

    boolean existsByAddress(String add);

    boolean existsByAddressAndCustomer(String add, Customer customer);

    Address findByAddressAndCustomer(String add, Customer customer);

    List<Address> findByCustomer(Customer customer);
}
