package com.dkdev.Journal.Application.controller;

import com.dkdev.Journal.Application.entity.User;
import com.dkdev.Journal.Application.service.UserDetailsServiceImpl;
import com.dkdev.Journal.Application.service.UserService;
import com.dkdev.Journal.Application.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
@Slf4j
public class PublicController {

    @Autowired
    private UserService userService ;

    @Autowired
    private AuthenticationManager authenticationManager ;

    @Autowired
    private UserDetailsServiceImpl userDetailsService ;

    @Autowired
    private JwtUtil jwtUtil ;

    @GetMapping("/health-check")
    public String healthcheck() {
        return "OK" ;
    }

    @PostMapping("/signup")
    public User signup(@RequestBody User newUser) {
        userService.saveNewUser(newUser) ;
        return newUser ;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login (@RequestBody User newUser) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(newUser.getUsername() , newUser.getPassword())) ;
            UserDetails userDetails = userDetailsService.loadUserByUsername(newUser.getUsername()) ;
            String jwt = jwtUtil.generateToken(userDetails.getUsername()) ;
            return new ResponseEntity<>(jwt , HttpStatus.OK) ;
        } catch (Exception e) {
            log.error("Exception ocurred while creating authentication token" , e) ;
            return new ResponseEntity<>("Incorrect usernamr or password" , HttpStatus.BAD_REQUEST) ;
        }
    }
}
