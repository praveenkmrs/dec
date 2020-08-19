package com.micropay.cards.system;

import com.micropay.cards.domains.User;
import com.micropay.cards.exceptions.UserNotFoundException;
import com.micropay.cards.system.domain.Contact;
import com.micropay.cards.system.domain.UserResponse;
import com.micropay.cards.system.domain.meta.Meta;
import com.micropay.cards.system.domain.meta.ResponseWrapper;
import com.micropay.cards.system.translators.UserServiceTranslator;
import com.micropay.cards.testUtils.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("User Service Tests")
class UserServiceTest {

    @Mock
    private UserServiceClient serviceClient;
    @Mock
    private UserServiceTranslator serviceTranslator;

    private UserService service;

    @BeforeEach
    void setUp() {
        service = new UserService(serviceClient, serviceTranslator);
    }

    @Test
    @DisplayName("Should successfully get User details")
    void shouldSuccessfullyGetUserDetails() {
        when(serviceClient.getData(anyLong())).thenReturn(
                ResponseWrapper.builder()
                        .meta(Meta.builder()
                                .code(Meta.Code.SUCCESS)
                                .description("Success")
                                .build())
                        .userResponse(UserResponse.builder()
                                .name("Name")
                                .id(1L)
                                .age(12)
                                .contact(Contact.builder()
                                        .address("wertegf")
                                        .phone("35421456")
                                        .email("asfr@aesr.asdf")
                                        .build())
                                .build())
                        .build());
        when(serviceTranslator.translateFromResponse(any(UserResponse.class))).thenReturn(TestData.getValidUser());
        User user = service.getUser(1L);
        assertNotNull(user);
        assertEquals("name", user.getName());
    }

    @Test
    @DisplayName("Should throe error when user not found")
    void shouldErrorOnGetUserDetailsWhenUserNotFound() {
        when(serviceClient.getData(anyLong())).thenReturn(
                ResponseWrapper.builder()
                        .meta(Meta.builder()
                                .code(Meta.Code.ERROR_USER_NOT_FOUND)
                                .description("Success")
                                .build())
                        .userResponse(null)
                        .build());
        assertThrows(UserNotFoundException.class, () -> service.getUser(1L));
    }
}