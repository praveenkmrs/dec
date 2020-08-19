package com.micropay.payments.resource.contracts;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.micropay.payments.resource.contracts.meta.Response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentResponse extends Response implements Serializable {
    private Long transactionId;
    private String cardNumber;
    private String status;
}
