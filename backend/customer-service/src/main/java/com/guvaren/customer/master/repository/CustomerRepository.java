package com.guvaren.customer.master.repository;

import com.guvaren.customer.master.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<CustomerEntity, String> {
}
