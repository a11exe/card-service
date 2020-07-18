package com.alllexe.cardservice.repository;

import com.alllexe.cardservice.model.Card;
import com.alllexe.cardservice.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CardRepository extends CrudRepository<Card, Integer> {
    List<Card> findByUser(User user);
}
