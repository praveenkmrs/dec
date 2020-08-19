package com.micropay.cards.datastore;

import com.micropay.cards.domains.Card;
import com.micropay.cards.domains.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface CardsRepository extends JpaRepository<Card, Long> {

    @Transactional
    public Integer deleteByIdAndUser(Long id, User user);
}
