package com.sppxs.europa.order.repository;

import com.sppxs.europa.order.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
