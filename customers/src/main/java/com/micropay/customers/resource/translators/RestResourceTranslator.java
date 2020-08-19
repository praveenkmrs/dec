package com.micropay.customers.resource.translators;

import com.micropay.customers.domains.Customer;
import com.micropay.customers.resource.contracts.Contact;
import com.micropay.customers.resource.contracts.UserRequest;
import com.micropay.customers.resource.contracts.UserResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class RestResourceTranslator {

    public UserResponse translateToContract(Customer dCustomer){
        return UserResponse.builder()
                .id(dCustomer.getId())
                .name(dCustomer.getName())
                .age(dCustomer.getAge())
                .contact(Contact.builder()
                        .address(dCustomer.getContact().getAddress())
                        .email(dCustomer.getContact().getEmail())
                        .phone(dCustomer.getContact().getPhone())
                        .build())
                .build();
    }

    public Customer translateToDomain(UserRequest request){
        return new Customer(
                request.getName(),
                LocalDate.parse(request.getDateOfBirth()),
                new com.micropay.customers.domains.Contact(
                        request.getEmail(),
                        request.getAddress(),
                        request.getPhone()
                ));
    }
}