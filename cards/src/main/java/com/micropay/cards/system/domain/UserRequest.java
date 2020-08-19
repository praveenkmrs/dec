package com.micropay.cards.system.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest implements Serializable {
    private Long id;
    private String name;
    private String dateOfBirth;
    private String email;
    private String address;
    private String phone;
}