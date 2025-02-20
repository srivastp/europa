package com.sppxs.europa.external.api;


import com.sppxs.europa.payment.entity.dto.PaymentRequest;
import com.sppxs.europa.payment.entity.dto.PaymentResponse;
import com.sppxs.europa.payment.service.PaymentProcessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@RestController
@RequestMapping("/apis/visa")
public class PaymentProcessorController {
    @Autowired
    private PaymentProcessorService paymentProcessorService;

    @PostMapping
    public List<PaymentResponse> processPayment(@RequestBody List<PaymentRequest> paymentRequests) throws ExecutionException, InterruptedException {
        try (ExecutorService executorService = Executors.newFixedThreadPool(10)) {
            List<Future<PaymentResponse>> futures = new ArrayList<>();

            paymentRequests.forEach(paymentRequest ->
                    futures.add(executorService.submit(() ->
                            paymentProcessorService.processPayment(paymentRequest)
                    ))
            );

            /*
            List<PaymentResponse> responses = new ArrayList<>();
            for (Future<PaymentResponse> future : futures) {
                PaymentResponse response = future.get(); // This will block until the response is available
                responses.add(response);
            }
            return responses;
            */

            List<CompletableFuture<PaymentResponse>> completableFutures = futures.stream()
                    .map(future -> CompletableFuture.supplyAsync(() -> {
                        try {
                            return future.get();
                        } catch (Exception e) {
                            throw new CompletionException(e);
                        }
                    }))
                    .toList();
            // Wait for all futures to complete
            CompletableFuture.allOf(
                    completableFutures.toArray(new CompletableFuture[0])).join();

            List<PaymentResponse> responses = completableFutures.stream()
                    .map(CompletableFuture::join)
                    .toList();
            return responses;
        }
    }
}
