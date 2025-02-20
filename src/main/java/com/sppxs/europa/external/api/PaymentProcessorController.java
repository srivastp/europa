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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@RestController
@RequestMapping("/apis/visa")
public class PaymentProcessorController {
    @Autowired
    private PaymentProcessorService paymentProcessorService;

    @PostMapping
    public List<PaymentResponse> processPayment(@RequestBody List<PaymentRequest> paymentRequests) throws ExecutionException, InterruptedException {
        //use Autocloseable to close the executor service
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<Future<PaymentResponse>> futures = new ArrayList<>();

        paymentRequests.forEach(paymentRequest ->
                futures.add(executorService.submit(() ->
                        paymentProcessorService.processPayment(paymentRequest)
                ))
        );

        List<PaymentResponse> responses = new ArrayList<>();

        for (Future<PaymentResponse> future : futures) {
            PaymentResponse response = future.get(); // This will block until the response is available
            responses.add(response);
        }
        executorService.shutdown();
        return responses;
    }
}
