package com.micropay.payments.service;

import com.micropay.payments.datastore.CardsRepository;
import com.micropay.payments.datastore.PaymentRepository;
import com.micropay.payments.domains.Card;
import com.micropay.payments.domains.Status;
import com.micropay.payments.domains.Transaction;
import com.micropay.payments.exceptions.CardNotFoundException;
import com.micropay.payments.exceptions.ExistingOpenTransactionException;
import com.micropay.payments.exceptions.NoOpenTransactionException;
import com.micropay.payments.system.cards.CardService;
import com.micropay.payments.utils.SelfValidating;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PaymentService {
    private CardService cardService;
    private PaymentRepository paymentRepository;
    private CardsRepository cardsRepository;

    public PaymentService(CardService cardService, PaymentRepository paymentRepository, CardsRepository cardsRepository) {
        this.cardService = cardService;
        this.paymentRepository = paymentRepository;
        this.cardsRepository = cardsRepository;
    }

    @Transactional
    public Transaction openPaymentTransaction(OpenPaymentCommand openPaymentCommand) {
        log.trace("Looking for open transactions on the card.");
        // check if there are any open transactions in the card
        Transaction openTransaction = paymentRepository.findByCard_NumberAndStatusEquals(new BigInteger(openPaymentCommand.getCardNumber()), Status.OPEN);
        if (null != openTransaction) {
            throw new ExistingOpenTransactionException("Open transaction exists on the card.");
        }
        Transaction newTransaction = new Transaction();
        newTransaction.openPaymentTransaction(openPaymentCommand.getPaymentAmount());
        Card card = cardsRepository.findByNumber(new BigInteger(openPaymentCommand.getCardNumber()));
        if (null == card) {
            // get list of cards owned by the customer
            List<Card> ownedCards = cardService.getCards(openPaymentCommand.getUserId());
            // check if the user owns the card
            Optional<Card> foundCard = ownedCards.stream().filter(c -> c.getNumber().equals(new BigInteger(openPaymentCommand.getCardNumber()))).findFirst();
            if (foundCard.isPresent()) {
                card = foundCard.get();
            } else {
                throw new CardNotFoundException("User does not own the card");
            }
        }
        card.addTransaction(newTransaction);
        newTransaction.setCard(card);
        return paymentRepository.save(newTransaction);
    }

    public Transaction closeTransaction(ClosePaymentCommand closePaymentCommand) {
        // check if there are any open transactions in the card
        Transaction transaction = paymentRepository.findByCard_NumberAndStatusEquals(new BigInteger(closePaymentCommand.getCardNumber()), Status.OPEN);
        if (null == transaction) {
            throw new NoOpenTransactionException("No Open transaction exists on the card.");
        }
        transaction.setStatus(Status.CLOSED);
        return paymentRepository.save(transaction);
    }

    @Getter
    public static class OpenPaymentCommand extends SelfValidating<OpenPaymentCommand> {
        @Min(1)
        private String cardNumber;
        @Min(1)
        private Long userId;
        @Min(1)
        @Digits(integer = 5, fraction = 2)
        private float paymentAmount;

        public OpenPaymentCommand(String cardNumber, Long userId, float paymentAmount) {
            this.cardNumber = cardNumber;
            this.userId = userId;
            this.paymentAmount = paymentAmount;
            this.validateSelf();
        }
    }

    @Getter
    public static class ClosePaymentCommand extends SelfValidating<ClosePaymentCommand> {
        @Min(1)
        private String cardNumber;
        @Min(1)
        private String transactionId;

        public ClosePaymentCommand(String cardNumber, String transactionId) {
            this.cardNumber = cardNumber;
            this.transactionId = transactionId;
            this.validateSelf();
        }
    }
}
