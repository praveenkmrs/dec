package com.micropay.payments.service;

import com.micropay.payments.datastore.CardsRepository;
import com.micropay.payments.datastore.PaymentRepository;
import com.micropay.payments.domains.Status;
import com.micropay.payments.domains.Transaction;
import com.micropay.payments.exceptions.CardNotFoundException;
import com.micropay.payments.exceptions.ExistingOpenTransactionException;
import com.micropay.payments.exceptions.NoOpenTransactionException;
import com.micropay.payments.system.cards.CardService;
import com.micropay.payments.testUtils.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Payments Service Tests")
class PaymentServiceTest {

    @Mock
    private CardService cardService;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private CardsRepository cardsRepository;

    private PaymentService service;

    @BeforeEach
    void setUp() {
        service = new PaymentService(cardService, paymentRepository, cardsRepository);
    }

    @Test
    @DisplayName("Should successfully open a payment transaction")
    void shouldSuccessfullyOpenPaymentTransaction() {
        when(paymentRepository.findByCard_NumberAndStatusEquals(any(BigInteger.class), any(Status.class))).thenReturn(null);
        when(cardsRepository.findByNumber(any(BigInteger.class))).thenReturn(TestData.getValidDomainCard());
        when(paymentRepository.save(any(Transaction.class))).thenReturn(TestData.getValidOpenTransaction());
        Transaction openTransaction = service.openPaymentTransaction(new PaymentService.OpenPaymentCommand("123", 1L, 123));
        assertNotNull(openTransaction);
        assertEquals(123, openTransaction.getPaymentAmount());
    }

    @Test
    @DisplayName("Should fail to open a payment transaction when an open transaction exists on the card")
    void shouldErrorOnOpenPaymentTransactionWhenExistingOpenTransaction() {
        when(paymentRepository.findByCard_NumberAndStatusEquals(any(BigInteger.class), any(Status.class))).thenReturn(TestData.getValidOpenTransaction());
        assertThrows(ExistingOpenTransactionException.class, () -> service.openPaymentTransaction(new PaymentService.OpenPaymentCommand("123", 1L, 123)));
    }

    @Test
    @DisplayName("Should successfully open a payment transaction by finding the user's new card")
    void shouldSuccessfullyOpenPaymentTransactionForNewCard() {
        when(paymentRepository.findByCard_NumberAndStatusEquals(any(BigInteger.class), any(Status.class))).thenReturn(null);
        when(cardsRepository.findByNumber(any(BigInteger.class))).thenReturn(null);
        when(cardService.getCards(anyLong())).thenReturn(Arrays.asList(TestData.getValidDomainCard()));
        when(paymentRepository.save(any(Transaction.class))).thenReturn(TestData.getValidOpenTransaction());
        Transaction openTransaction = service.openPaymentTransaction(new PaymentService.OpenPaymentCommand(BigInteger.TEN.toString(), 1L, 123));
        assertNotNull(openTransaction);
        assertEquals(123, openTransaction.getPaymentAmount());
    }

    @Test
    @DisplayName("Should fail to open a payment transaction if card is not found")
    void shouldErrorOnOpenPaymentTransactionWhenCardNotFound() {
        when(paymentRepository.findByCard_NumberAndStatusEquals(any(BigInteger.class), any(Status.class))).thenReturn(null);
        when(cardsRepository.findByNumber(any(BigInteger.class))).thenReturn(null);
        when(cardService.getCards(anyLong())).thenReturn(Arrays.asList(TestData.getValidDomainCard()));
        assertThrows(CardNotFoundException.class, () -> service.openPaymentTransaction(new PaymentService.OpenPaymentCommand(BigInteger.ONE.toString(), 1L, 123)));
    }

    @Test
    @DisplayName("Should successfully close a payment transaction")
    void shouldSuccessfullyCloseTransaction() {
        when(paymentRepository.findByCard_NumberAndStatusEquals(any(BigInteger.class), any(Status.class))).thenReturn(TestData.getValidOpenTransaction());
        when(paymentRepository.save(any(Transaction.class))).thenReturn(TestData.getValidCloseTransaction());
        Transaction closedTransaction = service.closeTransaction(new PaymentService.ClosePaymentCommand(BigInteger.TEN.toString(), "123"));
        assertNotNull(closedTransaction);
        assertEquals(123, closedTransaction.getPaymentAmount());
    }

    @Test
    @DisplayName("Should throw error on close a payment transaction when no open transaction is present")
    void shouldErrorOnCloseTransactionWhenNoExistingOpenTransaction() {
        when(paymentRepository.findByCard_NumberAndStatusEquals(any(BigInteger.class), any(Status.class))).thenReturn(null);
        assertThrows(NoOpenTransactionException.class, () -> service.closeTransaction(new PaymentService.ClosePaymentCommand(BigInteger.TEN.toString(), "123")));
    }
}