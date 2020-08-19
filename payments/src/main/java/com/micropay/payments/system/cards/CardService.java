package com.micropay.payments.system.cards;

import com.micropay.payments.domains.Card;
import com.micropay.payments.exceptions.UserNotFoundException;
import com.micropay.payments.system.cards.clients.CardServiceClient;
import com.micropay.payments.system.cards.domains.meta.Meta;
import com.micropay.payments.system.cards.domains.meta.ResponseWrapper;
import com.micropay.payments.system.cards.translators.CardServiceTranslator;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CardService {
    private static final String MAIN_SERVICE = "mainService";
    private CardServiceClient serviceClient;
    private CardServiceTranslator serviceTranslator;

    @Autowired
    public CardService(CardServiceClient serviceClient, CardServiceTranslator serviceTranslator) {
        this.serviceClient = serviceClient;
        this.serviceTranslator = serviceTranslator;
    }

    /**
     * Call card service to get list of cards owned by the customer if the customer exists else throw User not found exception
     *
     * @return User
     */
    @CircuitBreaker(name = MAIN_SERVICE, fallbackMethod = "fallback")
    public List<Card> getCards(Long id) {
        ResponseWrapper wrapper = serviceClient.getCards(id);
        if (null != wrapper.getMeta() && Meta.Code.SUCCESS.equals(wrapper.getMeta().getCode())) {
            return serviceTranslator.translateFromResponse(wrapper.getResponse());
        }
        throw new UserNotFoundException("User not found.");
    }

    private List<Card> fallback(Long id, Throwable e) {
        log.error("Failed to connect to user service. id: {}", id);
        throw new RuntimeException("Failed to connect to user service", e);
    }

}
