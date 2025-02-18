package com.sppxs.europa.payment.repository;

import com.sppxs.europa.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
