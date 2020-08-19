package com.micropay.customers.resource.translators;

import com.micropay.customers.domains.Contact;
import com.micropay.customers.domains.Customer;
import com.micropay.customers.resource.contracts.UserRequest;
import com.micropay.customers.resource.contracts.UserResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Customer Rest Resource Translator Tests")
class RestResourceTranslatorTest {

    private RestResourceTranslator translator = new RestResourceTranslator();

    @Test
    void translateToContract() {
        UserResponse respContract = translator.translateToContract(new TestData().getValidCustomer());
        assertEquals(1L, respContract.getId());
        assertEquals("Name", respContract.getName());
    }

    @Test
    void translateToDomain() {
        Customer custDomain = translator.translateToDomain(new TestData().getUserRequest());
        assertEquals(LocalDate.parse("2020-09-10"), custDomain.getDateOfBirth());
        assertEquals("Name", custDomain.getName());
    }

    private class TestData {
        public Customer getValidCustomer() {
            Contact contact = new Contact();
            contact.setAddress("123, asdfg, mnbvcx, poiujk - 6789054");
            contact.setPhone("9876543210");
            contact.setEmail("asdfg@qwert.bjn");
            Customer customer = new Customer();
            customer.setId(1L);
            customer.setName("Name");
            customer.setDateOfBirth(LocalDate.now());
            customer.setCreatedAt(new Date());
            customer.setUpdatedAt(new Date());
            customer.setContact(contact);
            return customer;
        }

        public UserRequest getUserRequest(){
            UserRequest req = new UserRequest();
            req.setId(1L);
            req.setName("Name");
            req.setAddress("123, asdfg, mnbvcx, poiujk - 6789054");
            req.setDateOfBirth("2020-09-10");
            req.setEmail("asdf@saefd.aef");
            req.setPhone("9087654321");
            return req;
        }
    }
}