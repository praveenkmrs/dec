package com.micropay.payments.resource.translators;

import com.micropay.payments.resource.contracts.PaymentResponse;
import com.micropay.payments.testUtils.TestData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
@DisplayName("Payments Rest Resource Translator Tests")
class RestResourceTranslatorTest {

    private RestResourceTranslator translator = new RestResourceTranslator();

    @Test
    void translateFromDomain() {
        PaymentResponse resp = translator.translateFromDomain(TestData.getValidOpenTransaction());
        assertNotNull(resp);
        assertEquals(BigInteger.TEN.toString(), resp.getCardNumber());
    }
}