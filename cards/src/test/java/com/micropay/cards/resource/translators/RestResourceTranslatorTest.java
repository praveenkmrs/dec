package com.micropay.cards.resource.translators;

import com.micropay.cards.domains.Card;
import com.micropay.cards.domains.CardType;
import com.micropay.cards.domains.User;
import com.micropay.cards.resource.contract.CardRequest;
import com.micropay.cards.resource.contract.CardResponse;
import com.micropay.cards.resource.contract.CardsResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Cards Rest Resource Translator Tests")
class RestResourceTranslatorTest {

    private RestResourceTranslator translator = new RestResourceTranslator();

    @Test
    void translateListOfCardsToContract() {
        CardsResponse resp = translator.translateToContract(new TestData().getCardsList());
        assertNotNull(resp);
        assertNotNull(resp.getCards());
        assertEquals(1, resp.getCards().size());
        assertEquals(123L, resp.getCards().get(0).getId());
    }

    @Test
    void translateCardToContract() {
        CardResponse resp = translator.translateToContract(new TestData().getValidCard());
        assertNotNull(resp);
        assertNotNull(resp.getCard());
        assertEquals(123L, resp.getCard().getId());
    }

    @Test
    void translateToDomain() {
        Card card = translator.translateToDomain(new TestData().getValidCardRequest());
        assertNotNull(card);
        assertEquals(new BigInteger("1234567890"), card.getNumber());
    }

    private class TestData {
        public Card getValidCard() {
            Card card = new Card();
            User user = new User();
            user.setId(123L);
            user.setName("name");
            card.setUser(user);
            card.setId(123L);
            card.setExpiresOn(LocalDate.now());
            card.setNumber(BigInteger.ONE);
            card.setType(CardType.MASTER_CARD);
            return card;
        }

        public List<Card> getCardsList(){
            return Arrays.asList(getValidCard());
        }

        public CardRequest getValidCardRequest(){
            CardRequest req = new CardRequest();
            req.setNumber("1234567890");
            req.setType(CardType.MASTER_CARD.getValue());
            req.setExpiresOn(LocalDate.now().toString());
            return req;
        }
    }
}