package com.sppxs.europa.payment.service;

import com.sppxs.europa.payment.entity.dto.TransactionRequest;
import com.sppxs.europa.payment.entity.dto.TransactionResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class ExternalPaymentServiceProcessor {
    private static final Logger logger = LoggerFactory.getLogger(ExternalPaymentServiceProcessor.class);

    @Autowired
    private RestTemplate restTemplate;


    //ToDO
    /*
    @Value("${payment.service.visa.url}")
    private String visaServiceUrl;
    @Value("${payment.service.timeout:5000}")
    private int timeout;
    */

    @Async
    public CompletableFuture<List<TransactionResponse>> processPayment(List<TransactionRequest> transactionRequests) {
        TransactionResponse[] responseArray;
        try {
            responseArray = restTemplate.postForObject(
                    "http://localhost:8080/apis/visa",
                    transactionRequests,
                    TransactionResponse[].class);
        } catch (RestClientException e) {
            //log.error("Error processing payment: {}", e.getMessage(), e);
            //throw new PaymentProcessingException("Failed to process payment", e);
            throw new RuntimeException("Failed to process payment", e);
        }
        logger.info("Received External payment service responses");
        //log.debug("Received {} payment responses", responseArray != null ? responseArray.length : 0);

        List<TransactionResponse> responses = responseArray != null ? Arrays.asList(responseArray) : new ArrayList<>();
        return CompletableFuture.completedFuture(responses);
    }
}
