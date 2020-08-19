package com.micropay.customers.service;

import com.micropay.customers.datastore.ContactRepository;
import com.micropay.customers.datastore.UserRepository;
import com.micropay.customers.domains.Customer;
import com.micropay.customers.exceptions.UserExists;
import com.micropay.customers.exceptions.UserNotFound;
import com.micropay.customers.testUtils.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Customer Service Tests")
class CustomerServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ContactRepository contactRepository;

    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        customerService = new CustomerService(userRepository, contactRepository);
    }

    @Test
    void shouldReturnAValidCustomer() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(TestData.getValidCustomer()));
        Customer cust = customerService.getCustomerById(1L);
        assertNotNull(cust);
        assertEquals(1L, cust.getId());
    }

    @Test
    void shouldThowUserNotFoundExceptionWhenCustomerNotFound() {
        when(userRepository.findById(anyLong())).thenThrow(new UserNotFound("User not found"));
        assertThrows(UserNotFound.class, () -> customerService.getCustomerById(1L));
    }

    @Test
    void shouldCreateACustomer() {
        when(userRepository.findByNameIgnoreCaseContainingAndDateOfBirth(anyString(), any(LocalDate.class))).thenReturn(Optional.empty());
        when(userRepository.save(any(Customer.class))).thenReturn(TestData.getValidCustomer());
        Customer cust = customerService.createCustomer(TestData.getValidCustomer());
        assertNotNull(cust);
        assertEquals(1L, cust.getId());
    }

    @Test
    void shouldThrowUserExistsExceptionOnCreateACustomer() {
        Customer cust = TestData.getValidCustomer();
        when(userRepository.findByNameIgnoreCaseContainingAndDateOfBirth(anyString(), any(LocalDate.class))).thenReturn(Optional.of(cust));
        assertThrows(UserExists.class, () -> customerService.createCustomer(cust));
    }

    @Test
    void shouldUpdateCustomer() {
        Customer uc = TestData.getValidCustomer();
        uc.getContact().setEmail("asd@poi.ce");
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(TestData.getValidCustomer()));
        when(userRepository.save(any(Customer.class))).thenReturn(uc);
        CustomerService.UpdateUserCommand update = new CustomerService.UpdateUserCommand(1L, "new name", "2020-03-12", uc.getContact().getEmail(), "765,address", "1234567890");
        Customer cust = customerService.updateCustomer(update);
        assertNotNull(cust);
        assertEquals(1L, cust.getId());
        assertEquals("asd@poi.ce", cust.getContact().getEmail());
    }

    @Test
    void shouldThrowUserNotFoundExceptionUpdateCustomer() {
        CustomerService.UpdateUserCommand cmd = new CustomerService.UpdateUserCommand(1L, "new name", "2020-03-12", "asd@poi.ce", "123,qwer,asdfdg,aefrg - 23", "9876543210");
        when(userRepository.findById(anyLong())).thenThrow(new UserNotFound("User not found"));
        assertThrows(UserNotFound.class, () -> customerService.updateCustomer(cmd));
    }

}