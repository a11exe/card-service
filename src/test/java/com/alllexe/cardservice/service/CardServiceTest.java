package com.alllexe.cardservice.service;

import com.alllexe.cardservice.model.Card;
import com.alllexe.cardservice.model.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class CardServiceTest {

    @Autowired
    private CardService cardService;

    @MockBean
    private RestService restService;

    @Test
    public void whenAddCardThanCardAdded() {
        User user = new User();
        user.setId(1);
        Card card = new Card();
        card.setUser(user);
        card.setCardNumber("78900");
        card.setBalance(BigDecimal.valueOf(150L));
        card.setExpireDate(LocalDateTime.of(2023, 5, 4, 0, 0, 0));
        assertFalse(cardService.findByUserId(1).contains(card));
        cardService.save(card);
        assertTrue(cardService.findByUserId(1).contains(card));
//        cardService.delete(card);
//        assertFalse(cardService.findByUserId(1).contains(card));
    }

    @Test
    public void whenUpdateCardThanCardUpdated() {
        User user = new User();
        user.setId(1);
        Card card = new Card();
        card.setUser(user);
        card.setCardNumber("333333 33 35");
        card.setBalance(BigDecimal.valueOf(150L));
        card.setExpireDate(LocalDateTime.of(2023, 5, 4, 0, 0, 0));
        Card cardDb = cardService.findByUserId(1).stream()
                .filter(c -> c.getCardNumber().equals(card.getCardNumber()))
                .findFirst().orElse(new Card()
        );
        assertThat(cardDb.getBalance()).isEqualTo("3333");
        cardService.updateByCardNumberAndUserId(card);
        cardDb = cardService.findByUserId(1).stream()
                .filter(c -> c.getCardNumber().equals(card.getCardNumber()))
                .findFirst().orElse(new Card()
                );
        assertThat(cardDb.getBalance()).isEqualTo("150");
    }

    @Test
    public void whenUpdateCardsByUserIdThenCardsUpdated() {

        User user = new User();
        user.setId(1);
        List<Card> cardsFact = new ArrayList<>();
        Card card1 = new Card();
        card1.setId(1);
        card1.setCardNumber("11111 11 11");
        card1.setExpireDate(LocalDateTime.parse("2022-09-19T09:28:20.594"));
        card1.setBalance(BigDecimal.valueOf(1000L));
        card1.setUser(user);
        Card card2 = new Card();
        card2.setId(1);
        card2.setCardNumber("333333 33 33");
        card2.setExpireDate(LocalDateTime.parse("2023-09-19T09:28:20.594"));
        card2.setBalance(BigDecimal.valueOf(3333L));
        card2.setUser(user);
        cardsFact.add(card1);
        cardsFact.add(card2);

        Mockito.doReturn(cardsFact)
                .when(restService)
                .getCardsFact("1");

        List<Card> actualCards = cardService.findByUserId(1);
        Card cardActual1 = actualCards.stream().filter(c->c.getCardNumber().equals(card1.getCardNumber())).findFirst().orElse(new Card());
        assertThat(cardActual1.getBalance()).isEqualTo(BigDecimal.valueOf(100L));
        assertFalse(actualCards.stream().anyMatch(c->c.getCardNumber().equals(card2.getCardNumber())));
        cardService.updateByUserId(1);

        List<Card> updatedCards = cardService.findByUserId(1);
        Card cardUpdated1 = updatedCards.stream().filter(c->c.getCardNumber().equals(card1.getCardNumber())).findFirst().orElse(new Card());
        assertThat(cardUpdated1.getBalance()).isEqualTo(card1.getBalance());
        assertTrue(updatedCards.stream().anyMatch(c->c.getCardNumber().equals(card2.getCardNumber())));

    }


}