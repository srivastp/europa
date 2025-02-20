package com.sppxs.europa.payment.service;

import com.sppxs.europa.order.entity.PurchaseOrder;
import com.sppxs.europa.payment.entity.Payment;
import com.sppxs.europa.payment.entity.PaymentTypeDetail;
import com.sppxs.europa.payment.entity.Transaction;
import com.sppxs.europa.payment.entity.dto.PaymentRequest;
import com.sppxs.europa.payment.entity.dto.PaymentResponse;
import com.sppxs.europa.payment.repository.PaymentRepository;
import com.sppxs.europa.payment.repository.PaymentTypeDetailRepository;
import com.sppxs.europa.payment.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private PaymentTypeDetailRepository paymentTypeDetailRepository;
    @Autowired
    private ExternalPaymentServiceProcessor externalPaymentServiceProcessor;

    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }

    public Payment createPayment(PurchaseOrder newPO) {
        try {
            Thread.sleep(3_000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //Create a payment type detail
        PaymentTypeDetail paymentTypeDetail = new PaymentTypeDetail();
        paymentTypeDetail.setCardNumber("8888-5678-1234-5678");
        paymentTypeDetail.setCardType("Mastercard");
        paymentTypeDetail.setExpiryDate("12/2024");
        paymentTypeDetail.setCvv("789");
        paymentTypeDetail.setCardHolderName("Mary Jane");

        PaymentTypeDetail ptd1 = paymentTypeDetailRepository.save(paymentTypeDetail);

        //Create a transaction with the payment type detail
        Transaction transaction1 = new Transaction();
        transaction1.setTransactionId(UUID.randomUUID().toString());
        transaction1.setAmount(newPO.getAmount());
        transaction1.setType("CREDIT");
        transaction1.setPaymentTypeDetail(ptd1);
        transaction1.setStatus("Pending");
        Set<Transaction> oldTransSet = new HashSet<>();
        oldTransSet.add(transaction1);

        //Create a payment with the transaction
        Payment payment = new Payment();
        payment.setPaymentId(UUID.randomUUID().toString());
        payment.setPurchaseOrderId(newPO.getGuid());
        payment.setUsername(newPO.getUsername());
        payment.addTransaction(transaction1);
        payment.setStatus("Pending");

        payment = paymentRepository.saveAndFlush(payment);
        Payment newPaymentObjCopy = payment;

        //Call the external payment service
        List<PaymentRequest> payload = payment.getTransactions().stream()
                .map(transaction -> generatePaymentRequestBuilder(transaction, newPaymentObjCopy)
                ).toList();

        //Process the payment
        Set<Transaction> transSet = new HashSet<>();
        externalPaymentServiceProcessor.processPayment(payload)
                .thenAccept(responses -> {
                    System.out.println("%%% Responses receiving here finally");
                    for (int i = 0; i < responses.size(); i++) {
                        PaymentResponse pr = responses.get(i);
                        Transaction transaction = transactionRepository.findByTransactionId(pr.getTransactionId()).getFirst();
                        if (responses.get(i).getStatus().equals("Success")) {
                            transaction.setStatus(responses.get(i).getStatus());
                            //transaction = transactionRepository.saveAndFlush(transaction);
                            //newPaymentObj.setStatus("Success");
                        } else if (responses.get(i).getStatus().equals("Declined")) {
                            transaction.setStatus(responses.get(i).getStatus());
                            //transaction = transactionRepository.saveAndFlush(transaction);
                        } else {
                            logStatus(responses.get(i).getStatus());
                        }
                        transSet.add(transaction);
                    }
                });


        //Update the status of the payment
        boolean isTransactionDeclined = transSet
                .stream()
                .anyMatch(
                        transaction -> transaction.getStatus().contains("Declined")
                );


        /*boolean isTransactionErroredOnService = transSet
                .stream()
                .anyMatch(
                        transaction -> transaction.getStatus().contains("Visa payment processing is down")
                );*/

        boolean isTransactionSuccessful = transSet
                .stream()
                .allMatch(
                        transaction -> transaction.getStatus().contains("Success")
                );

        if (isTransactionDeclined) {
            payment.setStatus("Declined");
        } else if (isTransactionSuccessful) {
            payment.setStatus("Success");
        } else {
            payment.setStatus("Pending");
        }

        payment.removeTransactions(oldTransSet);
        payment.addTransactions(transSet);

        payment = paymentRepository.saveAndFlush(payment);
        return payment;
    }

    private static PaymentRequest generatePaymentRequestBuilder(Transaction transaction, Payment newPaymentObjCopy) {
        return new PaymentRequest(
                newPaymentObjCopy.getPurchaseOrderId(),
                transaction.getTransactionId());
        /*PaymentRequest.builder()
                .transactionId(transaction.getTransactionId())
                .purchaseOrderId(newPaymentObjCopy.getPurchaseOrderId())
                .build();*/
    }

    private void logStatus(String s) {
        System.err.println(s);
    }

}
