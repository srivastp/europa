package com.sppxs.europa.payment.service;

import com.sppxs.europa.payment.entity.dto.TransactionRequest;
import com.sppxs.europa.payment.entity.dto.TransactionResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.sppxs.europa.payment.enums.Transaction.TransactionStatus.*;
import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;

@Service
@Slf4j
public class PaymentProcessorService {
    private static final Logger logger = LoggerFactory.getLogger(PaymentProcessorService.class);

    @Value("${payment.processor.timeout:8000}")
    private long processingTimeout;
    @Value("${payment.processor.success-threshold:3}")
    private int successThreshold;

    public TransactionResponse processPayment(TransactionRequest transactionRequest) {
        try {
            logger.info("Thread_Id: " + currentThread().threadId() +
                    " | Processing payment for Order: " + transactionRequest.getPurchaseOrderId() +
                    " | Transaction ID: " + transactionRequest.getTransactionId());
            sleep(8_000);
        } catch (InterruptedException e) {
            //currentThread().interrupt();
            throw new RuntimeException(e);
        }

        TransactionResponse transactionResponse;
        try {
            int random = (int) (Math.random() * 11);
            if (random == 7) {
                throw new RuntimeException("Visa payment processing is down. Cannot process payment at this time for Order: "
                        + transactionRequest.getPurchaseOrderId()
                        + " | Transaction ID: "
                        + transactionRequest.getTransactionId()
                );
            } else if (random > successThreshold) {
                logger.info("Thread_Id: " + currentThread().threadId() +
                        " | Payment processed successfully for Order: " + transactionRequest.getPurchaseOrderId() +
                        " | Transaction ID: " + transactionRequest.getTransactionId());
                transactionResponse = new TransactionResponse(
                        SUCCESS,
                        "0k_" + transactionRequest.getTransactionId(),
                        transactionRequest.getPurchaseOrderId(),
                        transactionRequest.getTransactionId()
                );
            } else {
                logger.info("Thread_Id: " + currentThread().threadId() +
                        " | Payment declined for Order: " + transactionRequest.getPurchaseOrderId() +
                        " | Transaction ID: " + transactionRequest.getTransactionId());
                transactionResponse = new TransactionResponse(
                        DECLINED,
                        null,
                        transactionRequest.getPurchaseOrderId(),
                        transactionRequest.getTransactionId()
                );
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            transactionResponse = new TransactionResponse(
                    SERVICE_ERROR,
                    null,
                    transactionRequest.getPurchaseOrderId(),
                    transactionRequest.getTransactionId()
            );
        }
        return transactionResponse;
    }

}
