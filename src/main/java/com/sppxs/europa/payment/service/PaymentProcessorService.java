package com.sppxs.europa.payment.service;

import com.sppxs.europa.payment.entity.dto.PaymentRequest;
import com.sppxs.europa.payment.entity.dto.PaymentResponse;
import org.springframework.stereotype.Service;

@Service
public class PaymentProcessorService {

    //@Value("${payment.processor.timeout:8000}")
    //private long processingTimeout;
    //@Value("${payment.processor.success-threshold:3}")
    //private int successThreshold;

    public PaymentResponse processPayment(PaymentRequest paymentRequest) {
        try {
            System.out.println("Thread_Id: " + Thread.currentThread().threadId() +
                    " | Processing payment for Order: " + paymentRequest.getPurchaseOrderId() +
                    " | Transaction ID: " + paymentRequest.getTransactionId());
            Thread.sleep(8_000);
        } catch (InterruptedException e) {
            //Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }

        PaymentResponse paymentResponse;
        try {
            int random = (int) (Math.random() * 11);
            if (random == 7) {
                throw new RuntimeException("Visa payment processing is down. Cannot process payment at this time for Order: "
                        + paymentRequest.getPurchaseOrderId()
                        + " | Transaction ID: "
                        + paymentRequest.getTransactionId()
                );
            } else if (random > 3) {
                System.out.println("Thread_Id: " + Thread.currentThread().threadId() +
                        " | Payment processed successfully for Order: " + paymentRequest.getPurchaseOrderId() +
                        " | Transaction ID: " + paymentRequest.getTransactionId());
                paymentResponse = new PaymentResponse(
                        "Success",
                        "0k_" + paymentRequest.getTransactionId(),
                        paymentRequest.getPurchaseOrderId(),
                        paymentRequest.getTransactionId()
                );
            } else {
                System.out.println("Thread_Id: " + Thread.currentThread().threadId() +
                        " | Payment declined for Order: " + paymentRequest.getPurchaseOrderId() +
                        " | Transaction ID: " + paymentRequest.getTransactionId());
                paymentResponse = new PaymentResponse(
                        "Declined",
                        null,
                        paymentRequest.getPurchaseOrderId(),
                        paymentRequest.getTransactionId()
                );
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            paymentResponse = new PaymentResponse(
                    e.getMessage(),
                    null,
                    paymentRequest.getPurchaseOrderId(),
                    paymentRequest.getTransactionId()
            );
        }
        return paymentResponse;
    }

}
