package com.sppxs.europa.shipping.repository;

import com.sppxs.europa.shipping.entity.ShippingDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShippingDetailsRepository extends JpaRepository<ShippingDetails, Long> {
}
