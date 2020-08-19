package com.micropay.cards.domains;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.time.LocalDate;

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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Digits(integer = 16, fraction = 0)
    private BigInteger number;
    @NotNull
    private CardType type;
    @FutureOrPresent(message = "Expiration date can only be future or present date")
    private LocalDate expiresOn;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;


    public Card(BigInteger number, CardType type, LocalDate expiresOn){
        this.number = number;
        this.type = type;
        this.expiresOn = expiresOn;
    }

}
