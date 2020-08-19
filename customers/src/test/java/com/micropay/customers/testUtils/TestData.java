package com.micropay.customers.testUtils;

import com.micropay.customers.domains.Contact;
import com.micropay.customers.domains.Customer;

import java.time.LocalDate;
import java.util.Date;

public class TestData {
    public static Customer getValidCustomer() {
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
}