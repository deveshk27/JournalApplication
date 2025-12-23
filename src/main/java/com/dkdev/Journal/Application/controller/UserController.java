package com.dkdev.Journal.Application.controller;

import com.dkdev.Journal.Application.entity.User;
import com.dkdev.Journal.Application.repository.UserRepository;
import com.dkdev.Journal.Application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository ;

    @GetMapping
    public List<User> getAll() {
        return userService.getAll() ;
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User newUser) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName() ;
        User oldUser = userService.findByUserName(username) ;
        oldUser.setUsername(newUser.getUsername());
        oldUser.setPassword(newUser.getPassword());
        userService.saveNewUser(oldUser) ;
        return new ResponseEntity<>(oldUser , HttpStatus.OK) ;
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName() ;
        userRepository.deleteByUsername(username) ;
        return new ResponseEntity<>("User deleted" , HttpStatus.OK) ;
    }
}
