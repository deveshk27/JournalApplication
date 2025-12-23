package com.dkdev.Journal.Application.controller;

import com.dkdev.Journal.Application.entity.User;
import com.dkdev.Journal.Application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private UserService userService ;

    @GetMapping("/health-check")
    public String healthcheck() {
        return "OK" ;
    }

    @PostMapping("/create-user")
    public User createUser(@RequestBody User newUser) {
        userService.saveNewUser(newUser) ;
        return newUser ;
    }
}
