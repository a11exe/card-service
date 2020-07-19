package com.alllexe.cardservice.repository;

import com.alllexe.cardservice.model.Card;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface CardRepository extends CrudRepository<Card, Integer> {
    @Query("SELECT c FROM card_fact c WHERE c.user.id = :id")
    List<Card> findByUserId(@Param("id") Integer id);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update card_fact c set c.expireDate =:expireDate, c.balance =:balance " +
            "where c.user.id = :userId and c.cardNumber = :cardNumber")
    void updateByCardNumberAndUserId(
            @Param("cardNumber") String cardNumber,
            @Param("userId") Integer userId,
            @Param("expireDate")LocalDateTime expireDate,
            @Param("balance") BigDecimal balance);
}
