package com.sppxs.europa.payment.service;

import com.sppxs.europa.order.entity.PurchaseOrder;
import com.sppxs.europa.payment.entity.Payment;
import com.sppxs.europa.payment.entity.PaymentTypeDetail;
import com.sppxs.europa.payment.entity.Transaction;
import com.sppxs.europa.payment.entity.dto.TransactionRequest;
import com.sppxs.europa.payment.entity.dto.TransactionResponse;
import com.sppxs.europa.payment.repository.PaymentRepository;
import com.sppxs.europa.payment.repository.PaymentTypeDetailRepository;
import com.sppxs.europa.payment.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.sppxs.europa.payment.enums.PaymentStatus.*;
import static com.sppxs.europa.payment.enums.Transaction.TransactionType.CREDIT;

@Service
@Slf4j
public class PaymentService {
    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);
    private final PaymentRepository paymentRepository;
    private final TransactionRepository transactionRepository;
    private final PaymentTypeDetailRepository paymentTypeDetailRepository;
    private final ExternalPaymentServiceProcessor externalPaymentServiceProcessor;

    public PaymentService(PaymentRepository paymentRepository,
                          TransactionRepository transactionRepository,
                          PaymentTypeDetailRepository paymentTypeDetailRepository,
                          ExternalPaymentServiceProcessor externalPaymentServiceProcessor) {
        this.paymentRepository = paymentRepository;
        this.transactionRepository = transactionRepository;
        this.paymentTypeDetailRepository = paymentTypeDetailRepository;
        this.externalPaymentServiceProcessor = externalPaymentServiceProcessor;
    }

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
        //ToDo: Storing full card numbers, CVVs, bank account details, and routing numbers in the
        // database can violate compliance requirements (e.g., PCI-DSS).
        // Consider masking or encrypting these fields, and never store CVV in plain text.
        PaymentTypeDetail paymentTypeDetail = new PaymentTypeDetail();
        paymentTypeDetail.setCardNumber("8888-5678-1234-5678");
        paymentTypeDetail.setCardType("Mastercard");
        paymentTypeDetail.setExpiryDate("12/2024");
        paymentTypeDetail.setCvv("789");
        paymentTypeDetail.setCardHolderName("Mary Jane");
        paymentTypeDetail.setCreatedAt(Instant.now());
        paymentTypeDetail.setUpdatedAt(Instant.now());

        PaymentTypeDetail ptd1 = paymentTypeDetailRepository.save(paymentTypeDetail);

        //Create a transaction with the payment type detail
        Transaction transaction1 = new Transaction();
        transaction1.setTransactionId(UUID.randomUUID().toString());
        transaction1.setAmount(newPO.getAmount());
        transaction1.setType(CREDIT);
        transaction1.setPaymentTypeDetail(ptd1);
        transaction1.setStatus(com.sppxs.europa.payment.enums.Transaction.TransactionStatus.PENDING);
        transaction1.setCreatedAt(Instant.now());
        transaction1.setUpdatedAt(Instant.now());
        Set<Transaction> oldTransSet = new HashSet<>();
        oldTransSet.add(transaction1);

        //Create a payment with the transaction
        Payment payment = new Payment();
        payment.setPaymentId(UUID.randomUUID().toString());
        payment.setPurchaseOrderId(newPO.getGuid());
        payment.setUsername(newPO.getUsername());
        payment.addTransaction(transaction1);
        payment.setCreatedAt(Instant.now());
        payment.setUpdatedAt(Instant.now());
        payment.setStatus(PENDING);

        payment = paymentRepository.saveAndFlush(payment);
        Payment newPaymentObjCopy = payment;

        //Call the external payment service
        List<TransactionRequest> payload = payment.getTransactions().stream()
                .map(transaction -> generatePaymentRequestBuilder(transaction, newPaymentObjCopy)
                ).toList();

        //Process the payment
        Set<Transaction> transSet = new HashSet<>();
        externalPaymentServiceProcessor.processPayment(payload)
                //.thenAccept(responses -> {
                .thenApply(responses -> {
                    logger.info("Evaluating External payment service responses.");
                    for (TransactionResponse pr : responses) {
                        Transaction transaction = transactionRepository.findByTransactionId(pr.getTransactionId()).getFirst();
                        if (pr.getStatus().equals(com.sppxs.europa.payment.enums.Transaction.TransactionStatus.SUCCESS)) {
                            transaction.setStatus(com.sppxs.europa.payment.enums.Transaction.TransactionStatus.SUCCESS);
                        } else if (pr.getStatus().equals(com.sppxs.europa.payment.enums.Transaction.TransactionStatus.DECLINED)) {
                            transaction.setStatus(com.sppxs.europa.payment.enums.Transaction.TransactionStatus.DECLINED);
                        } else {
                            logStatus(pr.getStatus().toString());
                        }
                        transSet.add(transaction);
                    }
                    return responses;
                }).join(); // block the current thread until responses are received


/*
        boolean isTransactionErroredOnService = transSet
                .stream()
                .anyMatch(
                        transaction -> transaction.getStatus().equals("Visa payment processing is down")
                );
*/

        //Update the status of the payment
        boolean isTransactionDeclined = transSet
                .stream()
                .anyMatch(
                        transaction -> transaction.getStatus().equals(com.sppxs.europa.payment.enums.Transaction.TransactionStatus.DECLINED)
                );

        boolean isTransactionSuccessful = transSet
                .stream()
                .allMatch(
                        transaction -> transaction.getStatus().equals(com.sppxs.europa.payment.enums.Transaction.TransactionStatus.SUCCESS)
                );

        if (isTransactionDeclined) {
            payment.setStatus(DECLINED);
        } else if (isTransactionSuccessful) {
            payment.setStatus(SUCCESS);
        } else {
            payment.setStatus(PENDING);
        }

        payment.removeTransactions(oldTransSet);
        payment.addTransactions(transSet);

        payment = paymentRepository.saveAndFlush(payment);
        return payment;
    }


    private static TransactionRequest generatePaymentRequestBuilder(Transaction transaction, Payment newPaymentObjCopy) {
        return new TransactionRequest(
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
