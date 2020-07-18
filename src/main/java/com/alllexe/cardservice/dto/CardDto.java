/*
 * @author Alexander Abramov (alllexe@mail.ru)
 * @version 1
 * @since 18.07.2020
 */
package com.alllexe.cardservice.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class CardDto {
    private Integer id;
    private String cardNumber;
    private LocalDateTime expireDate;
    private BigDecimal balance;
}
