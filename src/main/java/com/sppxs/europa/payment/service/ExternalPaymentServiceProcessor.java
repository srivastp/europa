package com.sppxs.europa.payment.service;

import com.sppxs.europa.payment.entity.dto.PaymentRequest;
import com.sppxs.europa.payment.entity.dto.PaymentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class ExternalPaymentServiceProcessor {
    @Autowired
    private RestTemplate restTemplate;

    @Async
    public CompletableFuture<List<PaymentResponse>> processPayment(List<PaymentRequest> paymentRequests) {
        PaymentResponse[] responseArray;
        responseArray = restTemplate.postForObject(
                "http://localhost:8080/apis/visa",
                paymentRequests,
                PaymentResponse[].class);
        System.out.println("%%% Responses receiving");

        List<PaymentResponse> responses = responseArray != null ? Arrays.asList(responseArray) : new ArrayList<>();
        return CompletableFuture.completedFuture(responses);
    }
}
