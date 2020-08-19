package com.micropay.payments.resource.translators;

import com.micropay.payments.domains.Transaction;
import com.micropay.payments.resource.contracts.PaymentResponse;
import org.springframework.stereotype.Component;

@Component
public class RestResourceTranslator {

    public PaymentResponse translateFromDomain(Transaction transaction) {
        return PaymentResponse.builder()
                .cardNumber(transaction.getCard().getNumber().toString())
                .status(transaction.getStatus().toString())
                .transactionId(transaction.getId())
                .build();
    }
}
