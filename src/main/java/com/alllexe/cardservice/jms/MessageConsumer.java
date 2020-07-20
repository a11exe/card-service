/*
 * @author Alexander Abramov (alllexe@mail.ru)
 * @version 1
 * @since 19.07.2020
 */
package com.alllexe.cardservice.jms;

import com.alllexe.cardservice.service.CardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@EnableJms
public class MessageConsumer {

    @Autowired
    private CardService cardService;

    @JmsListener(destination = "update-card-by-user-id")
    public void updateCardListByUserId(String message){
        log.info("Message received {} ", message);
        try {
            Integer userId = Integer.parseInt(message);
            cardService.updateByUserId(userId);
        } catch (NumberFormatException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
