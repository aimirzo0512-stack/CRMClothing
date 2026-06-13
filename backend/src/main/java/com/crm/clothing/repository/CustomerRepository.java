package com.crm.clothing.repository;

import com.crm.clothing.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Page<Customer> findByFullNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String n, String e, Pageable p);
}
