package com.sppxs.europa.external.api;


import com.sppxs.europa.payment.entity.dto.TransactionRequest;
import com.sppxs.europa.payment.entity.dto.TransactionResponse;
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
    public List<TransactionResponse> processPayment(@RequestBody List<TransactionRequest> transactionRequests) throws ExecutionException, InterruptedException {
        try (ExecutorService executorService = Executors.newFixedThreadPool(10)) {
            List<Future<TransactionResponse>> futures = new ArrayList<>();

            transactionRequests.forEach(transactionRequest ->
                    futures.add(executorService.submit(() ->
                            paymentProcessorService.processPayment(transactionRequest)
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

            // By default, CompletableFuture.supplyAsync uses the common ForkJoinPool. If you intend to use the
            // custom executorService you declared, pass it as a second argument:
            List<CompletableFuture<TransactionResponse>> completableFutures = futures.stream()
                    .map(future -> CompletableFuture.supplyAsync(() -> {
                        try {
                            return future.get();
                        } catch (Exception e) {
                            throw new CompletionException(e);
                        }
                    }, executorService))
                    .toList();
            // Wait for all futures to complete
            CompletableFuture.allOf(
                    completableFutures.toArray(new CompletableFuture[0])).join();

            List<TransactionResponse> responses = completableFutures.stream()
                    .map(CompletableFuture::join)
                    .toList();
            return responses;
        }
    }
}
