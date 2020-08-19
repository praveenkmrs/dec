package com.micropay.cards.service;

import com.micropay.cards.datastore.CardsRepository;
import com.micropay.cards.datastore.UserRepository;
import com.micropay.cards.domains.Card;
import com.micropay.cards.domains.User;
import com.micropay.cards.exceptions.CardAlreadyExistsException;
import com.micropay.cards.exceptions.CardNotFoundException;
import com.micropay.cards.system.UserService;
import com.micropay.cards.testUtils.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Cards Service Tests")
class CardServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private CardsRepository cardsRepository;
    @Mock
    private UserService userService;

    private CardService service;

    @BeforeEach
    void setUp() {
        service = new CardService(cardsRepository, userRepository, userService);
    }

    @Test
    @DisplayName("Should successfully get all cards of the user when user is present in local db")
    void shouldSuccessfullyGetCardsOfUserInLocalDataStore() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(TestData.getValidUser()));
        List<Card> cards = service.getCardsOfUser(1L);
        assertNotNull(cards);
        assertEquals(1, cards.size());
        assertEquals(123L, cards.get(0).getId());
    }

    @Test
    @DisplayName("Should successfully get all cards of the user when user is not present in local db")
    void shouldSuccessfullyGetCardsOfUserInUserService() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(userService.getUser(anyLong())).thenReturn(TestData.getValidUser());
        List<Card> cards = service.getCardsOfUser(1L);
        assertNotNull(cards);
        assertEquals(1, cards.size());
        assertEquals(123L, cards.get(0).getId());
    }

    @Test
    @DisplayName("Should successfully get card details when user is present in local db")
    void shouldSuccessfullyGetCardDetailsOfUserInLocalDataStore() {
        when(cardsRepository.findById(anyLong())).thenReturn(Optional.of(TestData.getValidCard()));
        Card card = service.getCardDetails(1L);
        assertNotNull(card);
        assertEquals(123L, card.getId());
    }

    @Test
    @DisplayName("Should throw exception when card is not found in local db")
    void shouldErrorOnGetCardDetailsWhenCardNotPresentInLocalDataStore() {
        when(cardsRepository.findById(anyLong())).thenThrow(new CardNotFoundException("Card not found"));
        assertThrows(CardNotFoundException.class, () -> service.getCardDetails(1L));
    }

    @Test
    @DisplayName("Should successfully add card to user when user is present in local db")
    void shouldSuccessfullyAddCardToUserInLocalDataStore() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(TestData.getValidUser()));
        lenient().when(userRepository.save(any(User.class))).thenReturn(TestData.getValidUser());
        Card card = TestData.getValidCard();
        card.setNumber(BigInteger.TEN);
        List<Card> cards = service.addCard(card, 1L);
        assertNotNull(cards);
        assertEquals(1, cards.size());
        assertEquals(123L, cards.get(0).getId());
    }

    @Test
    @DisplayName("Should prevent adding an existing card to user when user is present in local db")
    void shouldPreventAddingAnExistingCardToUserInLocalDataStore() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(TestData.getValidUser()));
        lenient().when(userRepository.save(any(User.class))).thenReturn(TestData.getValidUser());
        assertThrows(CardAlreadyExistsException.class, () -> service.addCard(TestData.getValidCard(), 1L));
    }

    @Test
    @DisplayName("Should successfully remove an existing card from user when user is present in local db")
    void shouldSuccessfullyRemoveCardToUserInLocalDataStore() {
        when(cardsRepository.existsById(anyLong())).thenReturn(Boolean.TRUE);
        when(cardsRepository.findById(anyLong())).thenReturn(Optional.of(TestData.getValidCard()));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(TestData.getValidUser()));
        service.removeCard(new CardService.RemoveUserCommand("123", "1"));
    }

    @Test
    @DisplayName("Should prevent removing a non existing card from user when user is present in local db")
    void shouldPreventAddingANonExistingCardFromUserInLocalDataStore() {
        when(cardsRepository.existsById(anyLong())).thenReturn(Boolean.FALSE);
        assertThrows(CardNotFoundException.class, () -> service.removeCard(new CardService.RemoveUserCommand("123", "1")));
    }
}