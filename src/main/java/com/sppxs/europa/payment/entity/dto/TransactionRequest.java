package com.sppxs.europa.payment.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class TransactionRequest {
    @NotNull(message = "Purchase order ID is required")
    @NotBlank(message = "Purchase order ID cannot be blank")
    private String purchaseOrderId;

    @NotNull(message = "Transaction ID is required")
    @NotBlank(message = "Transaction ID cannot be blank")
    private String transactionId;


}
