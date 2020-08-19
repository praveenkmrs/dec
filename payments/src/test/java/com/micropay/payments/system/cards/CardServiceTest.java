package com.micropay.payments.system.cards;

import com.micropay.payments.domains.Card;
import com.micropay.payments.exceptions.UserNotFoundException;
import com.micropay.payments.system.cards.clients.CardServiceClient;
import com.micropay.payments.system.cards.domains.CardsResponse;
import com.micropay.payments.system.cards.domains.meta.Meta;
import com.micropay.payments.system.cards.domains.meta.ResponseWrapper;
import com.micropay.payments.system.cards.translators.CardServiceTranslator;
import com.micropay.payments.testUtils.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Cards System Service Tests")
class CardServiceTest {

    @Mock
    private CardServiceClient serviceClient;
    @Mock
    private CardServiceTranslator serviceTranslator;

    private CardService service;

    @BeforeEach
    void setUp() {
        service = new CardService(serviceClient, serviceTranslator);
    }

    @Test
    @DisplayName("Should successfully get all cards of the user when user is present in local db")
    void shouldSuccessfullyGetCards() {
        when(serviceClient.getCards(anyLong())).thenReturn(
                ResponseWrapper.builder()
                        .meta(Meta.builder()
                                .code(Meta.Code.SUCCESS)
                                .description("Success")
                                .build())
                        .response(CardsResponse.builder()
                                .cards(Arrays.asList(TestData.getValidCard()))
                                .build())
                        .build());
        when(serviceTranslator.translateFromResponse(any(CardsResponse.class))).thenReturn(Arrays.asList(TestData.getValidDomainCard()));
        List<Card> cards = service.getCards(1L);
        assertNotNull(cards);
        assertEquals(1, cards.size());
        assertEquals(BigInteger.TEN, cards.get(0).getNumber());
    }

    @Test
    @DisplayName("Should throw error when user is not found")
    void shouldErrorOnGetCardsWhenUserNotFound() {
        when(serviceClient.getCards(anyLong())).thenReturn(
                ResponseWrapper.builder()
                        .meta(Meta.builder()
                                .code(Meta.Code.ERROR_USER_NOT_FOUND)
                                .description("No user")
                                .build())
                        .response(null)
                        .build());
        assertThrows(UserNotFoundException.class, () -> service.getCards(1L));
    }
}