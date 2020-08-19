package com.micropay.customers.domains;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.time.Period;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
@Table(name = "customers")
public class Customer extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank(message = "Name should not be blank")
    private String name;
    @Past(message = "Date of birth cannot be present or future date")
    private LocalDate dateOfBirth;
    @OneToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Contact contact;

    public Customer(String name, LocalDate dob, Contact contact) {
        this.name = name;
        this.dateOfBirth = dob;
        this.contact = contact;
    }

    public int getAge() {
        LocalDate dob = dateOfBirth;
        LocalDate now = LocalDate.now();
        return Period.between(dob, now).getYears();
    }
}