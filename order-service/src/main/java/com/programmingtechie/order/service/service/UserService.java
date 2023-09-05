package com.programmingtechie.order.service.service;


import com.programmingtechie.order.service.exception.UserNotFoundException;
import com.programmingtechie.order.service.model.User;
import com.programmingtechie.order.service.repository.UserRepository;
import com.programmingtechie.order.service.security.UserEntityDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;



    public User getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntityDetails userEntityDetails =(UserEntityDetails) authentication.getPrincipal();
        return userEntityDetails.getUser();
    }


    public Optional<User> findOptionalUserByUserName(String username) {
        return userRepository.findByUsername(username);
    }


    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
}
