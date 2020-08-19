package com.micropay.payments.domains;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
@Table(name = "transaction")
public class Transaction extends AuditModel{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "transaction_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Card card;
    @NotNull
    private Status status;
    @Digits(integer = 5, fraction = 2)
    private float paymentAmount;

    public Transaction openPaymentTransaction(float paymentAmount){
        this.paymentAmount = paymentAmount;
        this.status = Status.OPEN;
        return this;
    }

    public Transaction closePaymentTransaction(){
        this.status = Status.CLOSED;
        return this;
    }

}
