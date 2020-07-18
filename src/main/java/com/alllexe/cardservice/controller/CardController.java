/*
 * @author Alexander Abramov (alllexe@mail.ru)
 * @version 1
 * @since 18.07.2020
 */
package com.alllexe.cardservice.controller;

import com.alllexe.cardservice.model.Card;
import com.alllexe.cardservice.model.User;
import com.alllexe.cardservice.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CardController {

    @Autowired
    private CardService cardService;

    @GetMapping("/cards/user/{id}")
    public ResponseEntity<?> deleteContact(
            @PathVariable Integer id) {
        User user = new User();
        user.setId(id);
        List<Card> cards = cardService.findCardByUser(user);
        return new ResponseEntity<>(cards, HttpStatus.OK);
    }
}
