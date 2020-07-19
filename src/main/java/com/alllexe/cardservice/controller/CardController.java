/*
 * @author Alexander Abramov (alllexe@mail.ru)
 * @version 1
 * @since 18.07.2020
 */
package com.alllexe.cardservice.controller;

import com.alllexe.cardservice.dto.CardDto;
import com.alllexe.cardservice.model.Card;
import com.alllexe.cardservice.model.User;
import com.alllexe.cardservice.service.CardService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CardController {

    @Autowired
    private CardService cardService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/cards/user/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<CardDto> deleteContact(
            @PathVariable Integer id) {
         List<Card> cards = cardService.findByUserId(id);

        return cards.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private CardDto convertToDto(Card card) {
        return modelMapper.map(card, CardDto.class);
    }
}
