package com.sppxs.europa.shipping.repository;

import com.sppxs.europa.shipping.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
}
