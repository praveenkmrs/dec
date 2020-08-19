package com.micropay.cards.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.micropay.cards.domains.Card;
import com.micropay.cards.domains.CardType;
import com.micropay.cards.exceptions.CardAlreadyExistsException;
import com.micropay.cards.exceptions.CardNotFoundException;
import com.micropay.cards.exceptions.UserNotFoundException;
import com.micropay.cards.resource.contract.CardRequest;
import com.micropay.cards.resource.contract.CardResponse;
import com.micropay.cards.resource.contract.CardsResponse;
import com.micropay.cards.resource.translators.RestResourceTranslator;
import com.micropay.cards.service.CardService;
import com.micropay.cards.testUtils.TestData;
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

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {RestResource.class, CardService.class, RestResourceTranslator.class})
@WebMvcTest
@DisplayName("Cards Rest Resource Tests")
class RestResourceTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CardService service;
    @MockBean
    private RestResourceTranslator restResourceTranslator;

    @Test
    @DisplayName("Should Get all cards of the user")
    void shouldGetAllCardsOfUser() throws Exception {
        when(service.getCardsOfUser(anyLong())).thenReturn(TestData.getCardsList());
        when(restResourceTranslator.translateToContract(anyList()))
                .thenReturn(CardsResponse.builder()
                        .cards(
                                Arrays.asList(com.micropay.cards.resource.contract.Card.builder()
                                        .userId(123L)
                                        .type(CardType.MASTER_CARD.getValue())
                                        .number(BigInteger.ONE)
                                        .expiresOn(LocalDate.parse("2020-03-03"))
                                        .id(1L)
                                        .build())).build());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/v1/cards/user/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String resultDOW = result.getResponse().getContentAsString();
        assertNotNull(resultDOW);
        assertEquals("{\"meta\":{\"code\":\"SUCCESS\",\"description\":\"Successfully found the cards of the customer\"},\"response\":{\"cards\":[{\"id\":1,\"number\":1,\"type\":\"master_card\",\"expiresOn\":\"2020-03-03\",\"userId\":123}]}}", resultDOW);
    }

    @Test
    @DisplayName("Should throw error on Get all cards of the user when invalid user")
    void shouldErrorOnGetAllCardsOfUserWhenUserNotFound() throws Exception {
        when(service.getCardsOfUser(anyLong())).thenThrow(new UserNotFoundException("user not found"));
        when(restResourceTranslator.translateToContract(anyList()))
                .thenReturn(CardsResponse.builder()
                        .cards(
                                Arrays.asList(com.micropay.cards.resource.contract.Card.builder()
                                        .userId(123L)
                                        .type(CardType.MASTER_CARD.getValue())
                                        .number(BigInteger.ONE)
                                        .expiresOn(LocalDate.now())
                                        .id(1L)
                                        .build())).build());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/v1/cards/user/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        String resultDOW = result.getResponse().getContentAsString();
        assertNotNull(resultDOW);
        assertEquals("{\"meta\":{\"code\":\"CA400\",\"description\":\"Customer not found\"}}", resultDOW);
    }

    @Test
    @DisplayName("Should throw error on Get all cards of the user when card not found")
    void shouldErrorOnGetAllCardsOfUserWhenCardNOtFound() throws Exception {
        when(service.getCardsOfUser(anyLong())).thenThrow(new CardNotFoundException("Card not found"));
        when(restResourceTranslator.translateToContract(anyList()))
                .thenReturn(CardsResponse.builder()
                        .cards(
                                Arrays.asList(com.micropay.cards.resource.contract.Card.builder()
                                        .userId(123L)
                                        .type(CardType.MASTER_CARD.getValue())
                                        .number(BigInteger.ONE)
                                        .expiresOn(LocalDate.now())
                                        .id(1L)
                                        .build())).build());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/v1/cards/user/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        String resultDOW = result.getResponse().getContentAsString();
        assertNotNull(resultDOW);
        assertEquals("{\"meta\":{\"code\":\"CA402\",\"description\":\"Card not found\"}}", resultDOW);
    }

    @Test
    @DisplayName("Should successfully Get card details")
    void shouldGetCardDetails() throws Exception {
        when(service.getCardDetails(anyLong())).thenReturn(TestData.getValidCard());
        when(restResourceTranslator.translateToContract(any(Card.class)))
                .thenReturn(CardResponse.builder()
                        .card(com.micropay.cards.resource.contract.Card.builder()
                                .userId(123L)
                                .type(CardType.MASTER_CARD.getValue())
                                .number(BigInteger.ONE)
                                .expiresOn(LocalDate.parse("2020-03-03"))
                                .id(1L)
                                .build()).build());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/v1/cards/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String resultDOW = result.getResponse().getContentAsString();
        assertNotNull(resultDOW);
        assertEquals("{\"meta\":{\"code\":\"SUCCESS\",\"description\":\"Successfully found the card\"},\"response\":{\"card\":{\"id\":1,\"number\":1,\"type\":\"master_card\",\"expiresOn\":\"2020-03-03\",\"userId\":123}}}", resultDOW);
    }

    @Test
    @DisplayName("Should throw error when the user is not found")
    void shouldErrorOnGetCardDetailsWhenUserDoesNotExist() throws Exception {
        when(service.getCardDetails(anyLong())).thenThrow(new UserNotFoundException("User does not exists"));
        when(restResourceTranslator.translateToContract(any(Card.class)))
                .thenReturn(CardResponse.builder()
                        .card(com.micropay.cards.resource.contract.Card.builder()
                                .userId(123L)
                                .type(CardType.MASTER_CARD.getValue())
                                .number(BigInteger.ONE)
                                .expiresOn(LocalDate.now())
                                .id(1L)
                                .build()).build());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/v1/cards/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        String resultDOW = result.getResponse().getContentAsString();
        assertNotNull(resultDOW);
        assertEquals("{\"meta\":{\"code\":\"CA400\",\"description\":\"Customer not found\"}}", resultDOW);
    }

    @Test
    @DisplayName("Should throw error when card not found")
    void shouldErrorOnGetCardDetailsWhenCardDoesNotExists() throws Exception {
        when(service.getCardDetails(anyLong())).thenThrow(new CardNotFoundException("Card does not exists"));
        when(restResourceTranslator.translateToContract(any(Card.class)))
                .thenReturn(CardResponse.builder()
                        .card(com.micropay.cards.resource.contract.Card.builder()
                                .userId(123L)
                                .type(CardType.MASTER_CARD.getValue())
                                .number(BigInteger.ONE)
                                .expiresOn(LocalDate.now())
                                .id(1L)
                                .build()).build());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/v1/cards/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        String resultDOW = result.getResponse().getContentAsString();
        assertNotNull(resultDOW);
        assertEquals("{\"meta\":{\"code\":\"CA402\",\"description\":\"Card not found\"}}", resultDOW);
    }

    @Test
    @DisplayName("Should successfully Add card to user")
    void shouldAddCardToUser() throws Exception {
        when(service.addCard(any(Card.class), anyLong())).thenReturn(TestData.getCardsList());
        when(restResourceTranslator.translateToDomain(any(CardRequest.class))).thenReturn(TestData.getValidCard());
        when(restResourceTranslator.translateToContract(anyList()))
                .thenReturn(CardsResponse.builder()
                        .cards(Arrays.asList(com.micropay.cards.resource.contract.Card.builder()
                                        .userId(123L)
                                        .type(CardType.MASTER_CARD.getValue())
                                        .number(BigInteger.ONE)
                                        .expiresOn(LocalDate.parse("2020-03-03"))
                                        .id(1L)
                                        .build())).build());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/v1/cards/1")
                .content(new ObjectMapper().writeValueAsString(CardRequest.builder()
                        .type(CardType.MASTER_CARD.getValue())
                        .number(BigInteger.ONE.toString())
                        .expiresOn(LocalDate.now().toString())
                        .build()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String resultDOW = result.getResponse().getContentAsString();
        assertNotNull(resultDOW);
        assertEquals("{\"meta\":{\"code\":\"SUCCESS\",\"description\":\"Successfully added new card to the customer\"},\"response\":{\"cards\":[{\"id\":1,\"number\":1,\"type\":\"master_card\",\"expiresOn\":\"2020-03-03\",\"userId\":123}]}}", resultDOW);
    }

    @Test
    @DisplayName("Should throw error when card already exists with the user")
    void shouldThrowErrorOnAddCardToUseWhenCardAlreadyExists() throws Exception {
        when(service.addCard(any(Card.class), anyLong())).thenThrow(new CardAlreadyExistsException("Card already exists"));
        when(restResourceTranslator.translateToDomain(any(CardRequest.class))).thenReturn(TestData.getValidCard());
        when(restResourceTranslator.translateToContract(anyList()))
                .thenReturn(CardsResponse.builder()
                        .cards(Arrays.asList(com.micropay.cards.resource.contract.Card.builder()
                                .userId(123L)
                                .type(CardType.MASTER_CARD.getValue())
                                .number(BigInteger.ONE)
                                .expiresOn(LocalDate.now())
                                .id(1L)
                                .build())).build());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/v1/cards/1")
                .content(new ObjectMapper().writeValueAsString(CardRequest.builder()
                        .type(CardType.MASTER_CARD.getValue())
                        .number(BigInteger.ONE.toString())
                        .expiresOn(LocalDate.now().toString())
                        .build()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        String resultDOW = result.getResponse().getContentAsString();
        assertNotNull(resultDOW);
        assertEquals("{\"meta\":{\"code\":\"CA403\",\"description\":\"Card already exists\"}}", resultDOW);
    }

    @Test
    @DisplayName("Should throw error when user not found")
    void shouldThrowErrorOnAddCardToUseWhenUserNotFound() throws Exception {
        when(service.addCard(any(Card.class), anyLong())).thenThrow(new UserNotFoundException("User does not exists"));
        when(restResourceTranslator.translateToDomain(any(CardRequest.class))).thenReturn(TestData.getValidCard());
        when(restResourceTranslator.translateToContract(anyList()))
                .thenReturn(CardsResponse.builder()
                        .cards(Arrays.asList(com.micropay.cards.resource.contract.Card.builder()
                                .userId(123L)
                                .type(CardType.MASTER_CARD.getValue())
                                .number(BigInteger.ONE)
                                .expiresOn(LocalDate.now())
                                .id(1L)
                                .build())).build());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/v1/cards/1")
                .content(new ObjectMapper().writeValueAsString(CardRequest.builder()
                        .type(CardType.MASTER_CARD.getValue())
                        .number(BigInteger.ONE.toString())
                        .expiresOn(LocalDate.now().toString())
                        .build()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        String resultDOW = result.getResponse().getContentAsString();
        assertNotNull(resultDOW);
        assertEquals("{\"meta\":{\"code\":\"CA400\",\"description\":\"Customer does not exists\"}}", resultDOW);
    }

    @Test
    @DisplayName("Should successfully remove card from the user")
    void shouldRemoveCardFromUser() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/v1/cards/123/1")
                .content(new ObjectMapper().writeValueAsString(CardRequest.builder()
                        .type(CardType.MASTER_CARD.getValue())
                        .number(BigInteger.ONE.toString())
                        .expiresOn(LocalDate.now().toString())
                        .build()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String resultDOW = result.getResponse().getContentAsString();
        assertNotNull(resultDOW);
        assertEquals("{\"meta\":{\"code\":\"SUCCESS\",\"description\":\"Successfully removed the card from customer\"}}", resultDOW);
    }

    @Test
    @DisplayName("Should throw error when the card is not owned by the user")
    void shouldErrorOnRemoveCardFromUserWhenWrongCard() throws Exception {
        doThrow(new CardNotFoundException("No card found")).when(service).removeCard(any(CardService.RemoveUserCommand.class));
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/v1/cards/123/1")
                .content(new ObjectMapper().writeValueAsString(CardRequest.builder()
                        .type(CardType.MASTER_CARD.getValue())
                        .number(BigInteger.ONE.toString())
                        .expiresOn(LocalDate.now().toString())
                        .build()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        String resultDOW = result.getResponse().getContentAsString();
        assertNotNull(resultDOW);
        assertEquals("{\"meta\":{\"code\":\"CA402\",\"description\":\"Card not found\"}}", resultDOW);
    }

    @Test
    @DisplayName("Should throw error when the user is not found")
    void shouldErrorOnRemoveCardWhenOnWrongUser() throws Exception {
        doThrow(new UserNotFoundException("User not found")).when(service).removeCard(any(CardService.RemoveUserCommand.class));
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/v1/cards/123/1")
                .content(new ObjectMapper().writeValueAsString(CardRequest.builder()
                        .type(CardType.MASTER_CARD.getValue())
                        .number(BigInteger.ONE.toString())
                        .expiresOn(LocalDate.now().toString())
                        .build()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        String resultDOW = result.getResponse().getContentAsString();
        assertNotNull(resultDOW);
        assertEquals("{\"meta\":{\"code\":\"CA400\",\"description\":\"Customer not found\"}}", resultDOW);
    }
}