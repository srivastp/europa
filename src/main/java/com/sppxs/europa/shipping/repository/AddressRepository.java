package com.sppxs.europa.shipping.repository;

import com.sppxs.europa.shipping.entity.AddressInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<AddressInfo, Long> {
}
