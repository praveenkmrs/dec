package com.micropay.customers.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.micropay.customers.domains.Contact;
import com.micropay.customers.domains.Customer;
import com.micropay.customers.exceptions.UserExists;
import com.micropay.customers.exceptions.UserNotFound;
import com.micropay.customers.resource.contracts.UserRequest;
import com.micropay.customers.resource.contracts.UserResponse;
import com.micropay.customers.resource.handlers.GlobalExceptionHandler;
import com.micropay.customers.resource.translators.RestResourceTranslator;
import com.micropay.customers.service.CustomerService;
import com.micropay.customers.testUtils.TestData;
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

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {RestResource.class, CustomerService.class, RestResourceTranslator.class})
@WebMvcTest
@DisplayName("Customer Rest Resource Tests")
class RestResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService service;
    @MockBean
    private RestResourceTranslator restResourceTranslator;

    @Test
    void shouldGetSuccessResponseForAValidUser() throws Exception {
        when(service.getCustomerById(anyLong())).thenReturn(TestData.getValidCustomer());
        when(restResourceTranslator.translateToContract(any(Customer.class))).thenReturn(UserResponse.builder()
                .id(1L)
                .age(34)
                .name("name")
                .build());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/v1/customers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String resultDOW = result.getResponse().getContentAsString();
        assertNotNull(resultDOW);
        assertEquals("{\"meta\":{\"code\":\"SUCCESS\",\"description\":\"Successfully found the customer\"},\"userResponse\":{\"id\":1,\"name\":\"name\",\"contact\":null,\"age\":34}}", resultDOW);
    }

    @Test
    void shouldGetCustomerNotFoundErrorResponseWhenCustomerIsNotFound() throws Exception {
        when(service.getCustomerById(anyLong())).thenThrow(new UserNotFound("User not found"));
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/v1/customers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        String resultDOW = result.getResponse().getContentAsString();
        assertNotNull(resultDOW);
        assertEquals("{\"meta\":{\"code\":\"US400\",\"description\":\"Customer not found\"}}", resultDOW);
    }


    @Test
    void shouldSuccessfullyCreateUser() throws Exception {
        when(service.createCustomer(any(Customer.class))).thenReturn(TestData.getValidCustomer());
        when(restResourceTranslator.translateToContract(any(Customer.class))).thenReturn(UserResponse.builder()
                .id(1L)
                .age(34)
                .name("name")
                .build());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/v1/customers/")
                .content(new ObjectMapper().writeValueAsString(UserRequest.builder()
                        .id(1L)
                        .email("asdsd@adf.com")
                        .build()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String resultDOW = result.getResponse().getContentAsString();
        assertNotNull(resultDOW);
        assertEquals("{\"meta\":{\"code\":\"SUCCESS\",\"description\":\"Successfully created the customer\"}}", resultDOW);
    }

    @Test
    void shouldFailWhenTryingToCreateAnExistingUser() throws Exception {
        when(service.createCustomer(any(Customer.class))).thenThrow(new UserExists("User already exists"));
        when(restResourceTranslator.translateToDomain(any(UserRequest.class))).thenReturn(new Customer("name", LocalDate.now(), new Contact("asdas@asdf.asd", "123,sada,asda,asd-123", "9876543210")));
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/v1/customers/")
                .content(new ObjectMapper().writeValueAsString(UserRequest.builder()
                        .id(1L)
                        .email("asdsd@adf.com")
                        .build()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        String resultDOW = result.getResponse().getContentAsString();
        assertNotNull(resultDOW);
        assertEquals("{\"meta\":{\"code\":\"US402\",\"description\":\"Customer already exists\"}}", resultDOW);
    }

    @Test
    void shouldSuccessfullyUpdateUserDetails() throws Exception {
        when(service.updateCustomer(any(CustomerService.UpdateUserCommand.class))).thenReturn(TestData.getValidCustomer());
        when(restResourceTranslator.translateToContract(any(Customer.class))).thenReturn(UserResponse.builder()
                .id(1L)
                .age(34)
                .name("name")
                .build());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/v1/customers/1")
                .content(new ObjectMapper().writeValueAsString(UserRequest.builder()
                        .id(1L)
                        .email("asdsd@adf.com")
                        .build()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String resultDOW = result.getResponse().getContentAsString();
        assertNotNull(resultDOW);
        assertEquals("{\"meta\":{\"code\":\"SUCCESS\",\"description\":\"Successfully updated the customer\"},\"userResponse\":{\"id\":1,\"name\":\"name\",\"contact\":null,\"age\":34}}", resultDOW);
    }

    @Test
    void shouldFailWhenCustomerNotFoundToUpdateDetails() throws Exception {
        when(service.updateCustomer(any(CustomerService.UpdateUserCommand.class))).thenThrow(new UserNotFound("User not found"));
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/v1/customers/1")
                .content(new ObjectMapper().writeValueAsString(UserRequest.builder()
                        .id(1L)
                        .email("asdsd@adf.com")
                        .build()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        String resultDOW = result.getResponse().getContentAsString();
        assertNotNull(resultDOW);
        assertEquals("{\"meta\":{\"code\":\"US400\",\"description\":\"Customer not found\"}}", resultDOW);
    }
}