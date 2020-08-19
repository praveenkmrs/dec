package com.micropay.customers.resource.contracts;

import lombok.*;

import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse implements Serializable {
    private Long id;
    private String name;
    private Contact contact;
    private Integer age;
}