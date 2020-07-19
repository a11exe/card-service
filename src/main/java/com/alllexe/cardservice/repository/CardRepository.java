package com.alllexe.cardservice.repository;

import com.alllexe.cardservice.model.Card;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CardRepository extends CrudRepository<Card, Integer> {
    @Query("SELECT c FROM card_fact c WHERE c.user.id = :id")
    List<Card> findByUserId(@Param("id") Integer id);
}
