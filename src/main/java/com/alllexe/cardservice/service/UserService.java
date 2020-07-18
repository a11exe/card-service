/*
 * @author Alexander Abramov (alllexe@mail.ru)
 * @version 1
 * @since 18.07.2020
 */
package com.alllexe.cardservice.service;

import com.alllexe.cardservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

}
