package com.alllexe.cardservice.repository;

import com.alllexe.cardservice.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
}
