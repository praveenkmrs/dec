package com.micropay.payments.domains;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
@Table(name = "cards")
public class Card extends AuditModel {
    @Id
    private Long id;
    @Digits(integer = 16, fraction = 0)
    private BigInteger number;
    @NotNull
    private CardType type;
    @FutureOrPresent(message = "Expiration date can only be future or present date")
    private LocalDate expiresOn;
    @Min(1)
    private Long userId;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Transaction> transactions;

    public Card(Long id, BigInteger number, CardType type, LocalDate expiresOn, Long userId) {
        this.id = id;
        this.number = number;
        this.type = type;
        this.expiresOn = expiresOn;
        this.userId = userId;
    }

    public List<Transaction> addTransaction(Transaction transaction) {
        if (CollectionUtils.isEmpty(transactions)) {
            transactions = Collections.singletonList(transaction);
        } else {
            transactions.add(transaction);
        }
        return transactions;
    }
}
