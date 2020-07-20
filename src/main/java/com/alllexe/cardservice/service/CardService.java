/*
 * @author Alexander Abramov (alllexe@mail.ru)
 * @version 1
 * @since 18.07.2020
 */
package com.alllexe.cardservice.service;

import com.alllexe.cardservice.model.Card;
import com.alllexe.cardservice.model.User;
import com.alllexe.cardservice.repository.CardRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CardService {

    @Autowired
    private CardRepository repository;
    @Autowired
    RestService restService;

    public List<Card> findByUserId(Integer id) {
        return repository.findByUserId(id);
    }

    public Card save(Card card) {
        return repository.save(card);
    }

    public void delete(Card card) {
        repository.delete(card);
    }

    public void updateByCardNumberAndUserId(Card card) {
        repository.updateByCardNumberAndUserId(
                card.getCardNumber(), card.getUser().getId(), card.getExpireDate(), card.getBalance());
    }

    public boolean isCardMustBeUpdated(Card cardFact, Card cardActual) {
        return ((!cardFact.getBalance().equals(cardActual.getBalance()))
                || (!cardFact.getExpireDate().equals(cardActual.getExpireDate())));
    }

    public void updateByUserId(Integer userId) {

        User user = new User();
        user.setId(userId);

        List<Card> cardsActual = findByUserId(userId);
        List<Card> cardsFact = restService.getCardsFact(String.valueOf(userId));

        Map<String, Card> mapActualCards = cardsActual.stream().collect(
                Collectors.toMap(Card::getCardNumber, x -> x));
        Map<String, Card> mapFactCards = cardsFact.stream().collect(
                Collectors.toMap(Card::getCardNumber, x -> x));

        Set<String> updateCardNumbers = new HashSet<>();
        Set<String> equalCardNumbers = new HashSet<>();

        for (Map.Entry<String, Card> entry : mapFactCards.entrySet()
                ) {
            String cardNumber = entry.getKey();
            if (mapActualCards.containsKey(cardNumber)) {
                Card cardFact = mapFactCards.get(cardNumber);
                Card cardActual = mapActualCards.get(cardNumber);

                if (isCardMustBeUpdated(cardFact, cardActual)) {
                    updateCardNumbers.add(cardNumber);
                } else {
                    equalCardNumbers.add(cardNumber);
                }
                mapActualCards.remove(cardNumber);
            }
        }

        equalCardNumbers.forEach(mapFactCards::remove);

        mapActualCards.values().forEach(card -> {
            delete(card);
            log.info("Deleted card {}", card);
        });

        updateCardNumbers.forEach(cardNumber -> {
            Card cardFact = mapFactCards.get(cardNumber);
            cardFact.setUser(user);
            updateByCardNumberAndUserId(cardFact);
            log.info("Updated card {}", cardFact);
            mapFactCards.remove(cardNumber);
        });

        mapFactCards.values().forEach(card -> {
            card.setUser(user);
            card.setId(null);
            save(card);
            log.info("Added card {}", card);
        });
    }

}
