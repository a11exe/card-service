/*
 * @author Alexander Abramov (alllexe@mail.ru)
 * @version 1
 * @since 19.07.2020
 */
package com.alllexe.cardservice.jms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Queue;

@Slf4j
@Component
public class MessageProducer {
    @Autowired
    private Queue queue;

    @Autowired
    private JmsTemplate jmsTemplate;

    public void publish(String message){
        try {
            jmsTemplate.convertAndSend(queue, message);
        } catch (JmsException e) {
            log.error(e.getMessage());
        }
    }
}
