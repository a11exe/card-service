/*
 * @author Alexander Abramov (alllexe@mail.ru)
 * @version 1
 * @since 19.07.2020
 */
package com.alllexe.cardservice.jms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@EnableJms
public class MessageConsumer {
    @JmsListener(destination = "update-card-by-user-id")
    public void listener(String message){
        log.info("Message received {} ", message);
    }
}
