/*
 * @author Alexander Abramov (alllexe@mail.ru)
 * @version 1
 * @since 20.07.2020
 */
package com.alllexe.cardservice.service;

import com.alllexe.cardservice.dto.CardDto;
import com.alllexe.cardservice.model.Card;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
public class RestService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ModelMapper modelMapper;
    @Value("${cards.fact.resource.url}")
    private String cardsFactResourceUrl;

    public List<Card> getCardsFact(String message) {
        try {
            ResponseEntity<List<CardDto>> rateResponse =
                    restTemplate.exchange(cardsFactResourceUrl + "/" + message,
                            HttpMethod.GET, null, new ParameterizedTypeReference<List<CardDto>>() {
                            });

            List<CardDto> cardDtos = rateResponse.getBody();

            return Objects.requireNonNull(cardDtos).stream()
                    .map(this::convertToCard)
                    .collect(Collectors.toList());
        } catch (RestClientException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private Card convertToCard(CardDto cardDto) {
        return modelMapper.map(cardDto, Card.class);
    }
}
