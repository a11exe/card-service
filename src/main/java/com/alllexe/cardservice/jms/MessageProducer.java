/*
 * @author Alexander Abramov (alllexe@mail.ru)
 * @version 1
 * @since 19.07.2020
 */
package com.alllexe.cardservice.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Queue;

@Component
public class MessageProducer {
    @Autowired
    private Queue queue;

    @Autowired
    private JmsTemplate jmsTemplate;

    public void publish(String message){
        jmsTemplate.convertAndSend(queue, message);
    }
}
