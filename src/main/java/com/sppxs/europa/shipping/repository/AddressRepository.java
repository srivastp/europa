package com.sppxs.europa.shipping.repository;

import com.sppxs.europa.shipping.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
