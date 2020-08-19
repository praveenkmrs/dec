package com.micropay.payments.system.cards.domains;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Card implements Serializable {
    private Long id;
    private BigInteger number;
    private String type;
    private LocalDate expiresOn;
    private Long userId;
}
