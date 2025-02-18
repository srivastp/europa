package com.sppxs.europa.payment.service;

import com.sppxs.europa.payment.entity.Payment;
import com.sppxs.europa.payment.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }

    public List<Payment> createPayment(List<Payment> payments) {
        Payment payment = new Payment();
        payment.setPaymentId(UUID.randomUUID().toString());
        return paymentRepository.saveAll(payments);
    }
}
