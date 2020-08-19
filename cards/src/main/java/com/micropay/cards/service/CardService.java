package com.micropay.cards.service;

import com.micropay.cards.datastore.CardsRepository;
import com.micropay.cards.datastore.UserRepository;
import com.micropay.cards.domains.Card;
import com.micropay.cards.domains.User;
import com.micropay.cards.exceptions.CardAlreadyExistsException;
import com.micropay.cards.exceptions.CardNotFoundException;
import com.micropay.cards.system.UserService;
import com.micropay.cards.utils.SelfValidating;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CardService {
    private CardsRepository cardsRepository;
    private UserRepository userRepository;
    private UserService userService;

    @Autowired
    public CardService(CardsRepository cardsRepository, UserRepository userRepository, UserService userService) {
        this.cardsRepository = cardsRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public List<Card> getCardsOfUser(Long id) {
        log.trace("looking for cards of the user with id {} in DB", id);
        if (userRepository.existsById(id)) {
            Optional<User> dbUser = userRepository.findById(id);
            User foundUser = dbUser.get();
            log.debug("Count of cards owned by user with id {} is {} in DB", id, CollectionUtils.isEmpty(foundUser.getCards()) ? 0 : foundUser.getCards().size());
            return foundUser.getCards();
        } else {
            User foundUser = userService.getUser(id);
            log.debug("Count of cards owned by user with id {} is {} in DB", id, CollectionUtils.isEmpty(foundUser.getCards()) ? 0 : foundUser.getCards().size());
            return foundUser.getCards();
        }
    }

    public Card getCardDetails(Long id) {
        log.trace("looking for details of the card with id {} in DB", id);
        Card foundCard = cardsRepository.findById(id).orElseThrow(() -> new CardNotFoundException("Card not found."));
        return foundCard;
    }

    public List<Card> addCard(Card card, Long id) {
        User user = userRepository.findById(id).orElseGet(() -> userService.getUser(id));
        if (user.getCards().stream().anyMatch(c -> c.getNumber().equals(card.getNumber()))) {
            throw new CardAlreadyExistsException("Card already tagged to the user.");
        }
        card.setUser(user);
        user.addCard(card);
        User savedUser = userRepository.save(user);
        return savedUser.getCards();
    }

    public void removeCard(RemoveUserCommand removeUserCommand) {
        if (!cardsRepository.existsById(removeUserCommand.getCardId())) {
            throw new CardNotFoundException("Card not recognised");
        }
        Card foundCard = cardsRepository.findById(removeUserCommand.getCardId()).orElseThrow(() -> new CardNotFoundException("Card not found"));
        User foundUser = userRepository.findById(removeUserCommand.getUserId()).orElseGet(() -> userService.getUser(removeUserCommand.getUserId()));
        foundUser.removeCard(foundCard);
        userRepository.saveAndFlush(foundUser);
        cardsRepository.deleteByIdAndUser(removeUserCommand.getCardId(), foundUser);
    }

    @Getter
    public static class RemoveUserCommand extends SelfValidating<RemoveUserCommand> {
        @Min(1)
        private Long cardId;
        @Min(1)
        private Long userId;

        public RemoveUserCommand(String cardId, String userId) {
            this.cardId = Long.valueOf(cardId);
            this.userId = Long.valueOf(userId);
            this.validateSelf();
        }
    }
}