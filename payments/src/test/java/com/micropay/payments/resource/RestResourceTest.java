package com.micropay.payments.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.micropay.payments.domains.Status;
import com.micropay.payments.domains.Transaction;
import com.micropay.payments.exceptions.CardNotFoundException;
import com.micropay.payments.exceptions.ExistingOpenTransactionException;
import com.micropay.payments.exceptions.NoOpenTransactionException;
import com.micropay.payments.resource.contracts.PaymentRequest;
import com.micropay.payments.resource.contracts.PaymentResponse;
import com.micropay.payments.resource.translators.RestResourceTranslator;
import com.micropay.payments.service.PaymentService;
import com.micropay.payments.testUtils.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {RestResource.class, PaymentService.class, RestResourceTranslator.class})
@WebMvcTest
@DisplayName("Cards Rest Resource Tests")
class RestResourceTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PaymentService service;
    @MockBean
    private RestResourceTranslator restResourceTranslator;

    @Test
    @DisplayName("Should successfully open a transaction")
    void shouldSuccessfullyOpenPaymentTransaction() throws Exception {
        when(service.openPaymentTransaction(any(PaymentService.OpenPaymentCommand.class))).thenReturn(TestData.getValidOpenTransaction());
        when(restResourceTranslator.translateFromDomain(any(Transaction.class)))
                .thenReturn(PaymentResponse.builder()
                        .transactionId(1L)
                        .status(Status.OPEN.toString())
                        .cardNumber(BigInteger.TEN.toString())
                        .build());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/v1/payment/open/1")
                .content(new ObjectMapper().writeValueAsString(new PaymentRequest(BigInteger.TEN.toString(), BigDecimal.ONE)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String resultDOW = result.getResponse().getContentAsString();
        assertNotNull(resultDOW);
        assertEquals("{\"meta\":{\"code\":\"SUCCESS\",\"description\":\"Successfully added payment transaction to the card.\"},\"response\":{\"transactionId\":1,\"cardNumber\":\"10\",\"status\":\"OPEN\"}}", resultDOW);
    }

    @Test
    @DisplayName("Should throw error on open a transaction when card not found")
    void shouldErrorOnOpenPaymentTransactionWhenNoCardFound() throws Exception {
        when(service.openPaymentTransaction(any(PaymentService.OpenPaymentCommand.class))).thenThrow(new CardNotFoundException("Card not found"));
        when(restResourceTranslator.translateFromDomain(any(Transaction.class)))
                .thenReturn(PaymentResponse.builder()
                        .transactionId(1L)
                        .status(Status.OPEN.toString())
                        .cardNumber(BigInteger.TEN.toString())
                        .build());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/v1/payment/open/1")
                .content(new ObjectMapper().writeValueAsString(new PaymentRequest(BigInteger.TEN.toString(), BigDecimal.ONE)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        String resultDOW = result.getResponse().getContentAsString();
        assertNotNull(resultDOW);
        assertEquals("{\"meta\":{\"code\":\"PY401\",\"description\":\"Customer does not own the card.\"}}", resultDOW);
    }

    @Test
    @DisplayName("Should throw error on open a transaction when an existing open transaction is found")
    void shouldErrorOnOpenPaymentTransactionWhenExistingOpenTransaction() throws Exception {
        when(service.openPaymentTransaction(any(PaymentService.OpenPaymentCommand.class))).thenThrow(new ExistingOpenTransactionException("Existing open transaction"));
        when(restResourceTranslator.translateFromDomain(any(Transaction.class)))
                .thenReturn(PaymentResponse.builder()
                        .transactionId(1L)
                        .status(Status.OPEN.toString())
                        .cardNumber(BigInteger.TEN.toString())
                        .build());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/v1/payment/open/1")
                .content(new ObjectMapper().writeValueAsString(new PaymentRequest(BigInteger.TEN.toString(), BigDecimal.ONE)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotAcceptable())
                .andReturn();

        String resultDOW = result.getResponse().getContentAsString();
        assertNotNull(resultDOW);
        assertEquals("{\"meta\":{\"code\":\"PY400\",\"description\":\"An open transaction exists on the card. Cannot make another transaction.\"}}", resultDOW);
    }

    @Test
    @DisplayName("Should successfully close an open transaction")
    void shouldSuccessfullyClosePaymentTransaction() throws Exception {
        when(service.closeTransaction(any(PaymentService.ClosePaymentCommand.class))).thenReturn(TestData.getValidCloseTransaction());
        when(restResourceTranslator.translateFromDomain(any(Transaction.class)))
                .thenReturn(PaymentResponse.builder()
                        .transactionId(1L)
                        .status(Status.CLOSED.toString())
                        .cardNumber(BigInteger.TEN.toString())
                        .build());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/v1/payment/close/1/1")
                .content(new ObjectMapper().writeValueAsString(new PaymentRequest(BigInteger.TEN.toString(), BigDecimal.ONE)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String resultDOW = result.getResponse().getContentAsString();
        assertNotNull(resultDOW);
        assertEquals("{\"meta\":{\"code\":\"SUCCESS\",\"description\":\"Successfully closed payment transaction to the card.\"},\"response\":{\"transactionId\":1,\"cardNumber\":\"10\",\"status\":\"CLOSED\"}}", resultDOW);
    }

    @Test
    @DisplayName("Should throw error on closing an open transaction when no existing open transaction present")
    void shouldErrorOnClosePaymentTransactionWhenNoExistingOpenTransaction() throws Exception {
        when(service.closeTransaction(any(PaymentService.ClosePaymentCommand.class))).thenThrow(new NoOpenTransactionException("No existing open transactions"));
        when(restResourceTranslator.translateFromDomain(any(Transaction.class)))
                .thenReturn(PaymentResponse.builder()
                        .transactionId(1L)
                        .status(Status.CLOSED.toString())
                        .cardNumber(BigInteger.TEN.toString())
                        .build());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/v1/payment/close/1/1")
                .content(new ObjectMapper().writeValueAsString(new PaymentRequest(BigInteger.TEN.toString(), BigDecimal.ONE)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        String resultDOW = result.getResponse().getContentAsString();
        assertNotNull(resultDOW);
        assertEquals("{\"meta\":{\"code\":\"PY402\",\"description\":\"No open payment requests to close.\"}}", resultDOW);
    }
}