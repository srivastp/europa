package com.sppxs.europa.payment.api;

import com.sppxs.europa.order.entity.PurchaseOrder;
import com.sppxs.europa.payment.entity.Payment;
import com.sppxs.europa.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @GetMapping
    public ResponseEntity<List<Payment>> getPayments() {
        List<Payment> payments = paymentService.findAll();
        return ResponseEntity.ok(payments);
    }

    @PostMapping
    public List<Payment> createPayment(@RequestBody List<Payment> payments) {
        return paymentService.createPayment(payments);
    }
}
