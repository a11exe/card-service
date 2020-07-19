/*
 * @author Alexander Abramov (alllexe@mail.ru)
 * @version 1
 * @since 19.07.2020
 */
package com.alllexe.cardservice.jms;

import com.alllexe.cardservice.dto.CardDto;
import com.alllexe.cardservice.model.Card;
import com.alllexe.cardservice.model.User;
import com.alllexe.cardservice.service.CardService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Slf4j
@EnableJms
public class MessageConsumer {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CardService cardService;

    @Value("${cards.fact.resource.url}")
    private String cardsFactResourceUrl;

    @JmsListener(destination = "update-card-by-user-id")
    public void updateCardListByUserId(String message){
        log.info("Message received {} ", message);
        User user = new User();
        // TODO check Number format exception
        user.setId(Integer.parseInt(message));
//        ResponseEntity<String> response
//                = restTemplate.getForEntity(cardsFactResourceUrl + "/" + message, String.class);
//        log.info(response.getBody());
        // TODO check parse response exception
        ResponseEntity<List<CardDto>> rateResponse =
                restTemplate.exchange(cardsFactResourceUrl + "/" + message,
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<CardDto>>() {
                        });
        List<CardDto> cardDtos = rateResponse.getBody();
        // TODO check convert exception
        List<Card> cards = Objects.requireNonNull(cardDtos).stream()
                .map(this::convertToCard)
                .collect(Collectors.toList());
        cards.forEach(card -> card.setUser(user));

        cards.forEach(card -> log.info(card.toString()));
        // TODO make full update
        cards.forEach(card -> cardService.save(card));

    }

    private Card convertToCard(CardDto cardDto) {
        return modelMapper.map(cardDto, Card.class);
    }
}
