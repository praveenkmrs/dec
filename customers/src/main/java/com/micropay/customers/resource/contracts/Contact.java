package com.micropay.customers.resource.contracts;

import lombok.*;

import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contact implements Serializable {
    private String email;
    private String address;
    private String phone;
}
