package com.sppxs.europa.payment.service;

import com.sppxs.europa.payment.entity.dto.PaymentRequest;
import com.sppxs.europa.payment.entity.dto.PaymentResponse;
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
public class ExternalPaymentServiceProcessor {
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
    public CompletableFuture<List<PaymentResponse>> processPayment(List<PaymentRequest> paymentRequests) {
        PaymentResponse[] responseArray;
        try {
            responseArray = restTemplate.postForObject(
                    "http://localhost:8080/apis/visa",
                    paymentRequests,
                    PaymentResponse[].class);
        } catch (RestClientException e) {
            //log.error("Error processing payment: {}", e.getMessage(), e);
            //throw new PaymentProcessingException("Failed to process payment", e);
            throw new RuntimeException("Failed to process payment", e);
        }
        System.out.println("%%% Responses receiving");
        //log.debug("Received {} payment responses", responseArray != null ? responseArray.length : 0);

        List<PaymentResponse> responses = responseArray != null ? Arrays.asList(responseArray) : new ArrayList<>();
        return CompletableFuture.completedFuture(responses);
    }
}
