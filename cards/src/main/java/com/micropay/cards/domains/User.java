package com.micropay.cards.domains;

import com.micropay.cards.exceptions.CardNotFoundException;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
@Table(name = "users")
public class User extends AuditModel {
    @Id
    private Long id;
    @NotBlank(message = "Name should not be blank")
    private String name;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Card> cards;

    public User(Long id, String name, List<Card> cards){
        this.id = id;
        this.name = name;
        this.cards = cards;
    }

    public User(Long id){
        this.id = id;
    }

    public List<Card> addCard(Card card) {
        if (CollectionUtils.isEmpty(cards)) {
            cards = new ArrayList<>();
            cards.add(card);
        } else {
            cards.add(card);
        }
        return cards;
    }

    public List<Card> removeCard(Card card) {
        if (CollectionUtils.isEmpty(cards)) {
            throw new CardNotFoundException("No cards present for the user.");
        } else if (cards.stream().anyMatch(c -> c.getId().equals(card.getId()))) {
            cards.remove(card);
        } else {
            throw new CardNotFoundException("Card not owned by the user.");
        }
        return cards;
    }
}
