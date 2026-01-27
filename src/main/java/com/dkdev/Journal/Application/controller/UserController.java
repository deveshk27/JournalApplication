package com.dkdev.Journal.Application.controller;

import com.dkdev.Journal.Application.api.response.QuotesResponse;
import com.dkdev.Journal.Application.api.response.WeatherResponse;
import com.dkdev.Journal.Application.entity.User;
import com.dkdev.Journal.Application.repository.UserRepository;
import com.dkdev.Journal.Application.service.QuotesService;
import com.dkdev.Journal.Application.service.UserService;
import com.dkdev.Journal.Application.service.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jdk.jshell.execution.JdiExecutionControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@Tag(name = "User APIs" , description = "Read, Update and Delete User")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository ;

    @Autowired
    private WeatherService weatherService ;

    @Autowired
    private QuotesService quotesService ;

    @GetMapping
    @Operation(summary = "Get a list of all the users ")
    public List<User> getAll() {
        return userService.getAll() ;
    }

    @PutMapping
    @Operation(summary = "Update the details of a user")
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
    @Operation(summary = "Delete a User")
    public ResponseEntity<?> deleteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName() ;
        userRepository.deleteByUsername(username) ;
        return new ResponseEntity<>("User deleted" , HttpStatus.OK) ;
    }

    @GetMapping("/about")
    @Operation(summary = "Get the greeting for a user")
    public ResponseEntity<?> greeting() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName() ;
        WeatherResponse weatherResponse = weatherService.getWeather("Mumbai") ;
        QuotesResponse[] quotesResponse = quotesService.getQuote() ;
        String quote = "" ;
        String greeting = "";
        if(weatherResponse != null) {
            greeting = ", weather feels like " + weatherResponse.getCurrent().getFeelslike() ;
        }
        if (quotesResponse != null && quotesResponse.length > 0) {
            quote = quotesResponse[0].getQuote();
        } else {
            quote = "Default quote: coding is fun!"; // Fallback if API returns nothing
        }
        return new ResponseEntity<>("Hi " + username + greeting + ". Todays quote is : " + quote , HttpStatus.OK) ;
    }
}
