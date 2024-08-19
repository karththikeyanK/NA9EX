package com.express.user.repo;

import com.express.user.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByMobile(String mobile);

    Customer findByMobile(String mobile);
}
