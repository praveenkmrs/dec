package com.micropay.cards.system.translators;

import com.micropay.cards.domains.User;
import com.micropay.cards.system.domain.Contact;
import com.micropay.cards.system.domain.UserResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Cards Service Tests")
class UserServiceTranslatorTest {

    private UserServiceTranslator translator = new UserServiceTranslator();

    @Test
    @DisplayName("Successfully translate from user response")
    void translateFromResponse() {
        User user = translator.translateFromResponse(UserResponse.builder()
                .age(12)
                .id(1L)
                .name("name")
                .contact(Contact.builder()
                        .email("asdf@as.fa")
                        .phone("9876543210")
                        .address("123, asd,asd,asd,asd - 1234")
                        .build())
                .build());
        assertNotNull(user);
        assertEquals("name", user.getName());
    }
}