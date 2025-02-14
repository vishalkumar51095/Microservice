package com.vishalkumar.user.service.services.impl;

import com.vishalkumar.user.service.entities.User;
import com.vishalkumar.user.service.repositories.UserRepository;
import com.vishalkumar.user.service.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public User saveUser(User user) {
        //generate unique userid
        String randomUserid = UUID.randomUUID().toString();
        user.setUserId(randomUserid);
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public User getUser(String userId) {
        return userRepository.findById(userId).orElseThrow(()-> new RuntimeException("User with given user id is not found on server !!:."+userId));
    }
}
