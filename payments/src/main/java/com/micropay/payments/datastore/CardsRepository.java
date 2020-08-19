package com.micropay.payments.datastore;

import com.micropay.payments.domains.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface CardsRepository extends JpaRepository<Card, Long> {
    public Card findByNumber(BigInteger number);
}
