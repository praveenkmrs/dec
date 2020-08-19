package com.micropay.payments.system.cards.translators;

import com.micropay.payments.domains.Card;
import com.micropay.payments.system.cards.domains.CardsResponse;
import com.micropay.payments.testUtils.TestData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
@DisplayName("Cards service Translator Tests")
class CardServiceTranslatorTest {

    private CardServiceTranslator translator = new CardServiceTranslator();

    @Test
    void translateFromResponse() {
        List<Card> cards = translator.translateFromResponse(CardsResponse.builder().cards(Arrays.asList(TestData.getValidCard())).build());
        assertNotNull(cards);
        assertEquals(1, cards.size());
        assertEquals(123L, cards.get(0).getId());
    }
}