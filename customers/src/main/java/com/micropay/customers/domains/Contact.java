package com.micropay.customers.domains;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
@Table(name = "contacts")
public class Contact extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank
    @Email(message = "Invalid email")
    private String email;
    @NotBlank
    @Size(min = 10, max = 100, message = "Address length should be with in 10 to 100")
    private String address;
    @NotBlank
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Phone number should be of 10 digits")
    private String phone;
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "contact", optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Customer customer;

    public Contact(String email, String addr, String ph) {
        this.email = email;
        this.address = addr;
        this.phone = ph;
    }

}
