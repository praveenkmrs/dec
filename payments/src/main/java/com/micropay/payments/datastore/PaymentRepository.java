package com.micropay.payments.datastore;

import com.micropay.payments.domains.Status;
import com.micropay.payments.domains.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface PaymentRepository extends JpaRepository<Transaction, Long> {
    public Transaction findByCard_NumberAndStatusEquals(BigInteger cardNumber, Status status);
}
