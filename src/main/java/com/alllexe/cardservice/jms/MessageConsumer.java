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
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
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
        try {
            Integer userId = Integer.parseInt(message);
            User user = new User();
            user.setId(userId);

            List<Card> cardsActual = cardService.findByUserId(userId);

            List<Card> cardsFact = getCardsFact(message);


            Map<String, Card> mapActulCards = cardsActual.stream().collect(
                    Collectors.toMap(Card::getCardNumber, x -> x));

            Map<String, Card> mapFactCards = cardsFact.stream().collect(
                    Collectors.toMap(Card::getCardNumber, x -> x));

            Set<String> updateCardNumbers = new HashSet<>();

            for (Map.Entry<String, Card> entry: mapFactCards.entrySet()
                 ) {
                String cardNumber = entry.getKey();
                if (mapActulCards.containsKey(cardNumber)) {
                    updateCardNumbers.add(cardNumber);
                    mapActulCards.remove(cardNumber);
                }
            }

            mapActulCards.values().forEach(card -> {
                cardService.delete(card);
                log.info("Deleted card {}", card);
            });

            updateCardNumbers.forEach(cardNumber -> {
                Card card = mapFactCards.get(cardNumber);
                card.setUser(user);
                cardService.updateByCardNumberAndUserId(card);
                log.info("Updated card {}", card);
                mapFactCards.remove(cardNumber);
            });

            mapFactCards.values().forEach(card -> {
                card.setUser(user);
                card.setId(null);
                cardService.save(card);
                log.info("Saved card {}", card);
            });


        } catch (NumberFormatException | RestClientException | NullPointerException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }

    }

    private List<Card> getCardsFact(String message) {
        ResponseEntity<List<CardDto>> rateResponse =
                restTemplate.exchange(cardsFactResourceUrl + "/" + message,
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<CardDto>>() {
                        });

        List<CardDto> cardDtos = rateResponse.getBody();

        return Objects.requireNonNull(cardDtos).stream()
                .map(this::convertToCard)
                .collect(Collectors.toList());
    }

    private Card convertToCard(CardDto cardDto) {
        return modelMapper.map(cardDto, Card.class);
    }
}
