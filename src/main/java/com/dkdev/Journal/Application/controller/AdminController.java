package com.dkdev.Journal.Application.controller;

import com.dkdev.Journal.Application.cache.Appcache;
import com.dkdev.Journal.Application.entity.User;
import com.dkdev.Journal.Application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.ResourceBundle;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService ;

    @Autowired
    private Appcache appcache ;

    @GetMapping("/all-users")
    public ResponseEntity<?> getAllUsers() {
        List<User> all = userService.getAll() ;
        if(all != null & !all.isEmpty()) {
            return new ResponseEntity<>(all,HttpStatus.OK) ;
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND) ;
    }

    @PostMapping("/create-admin-user")
    public void createAdmin(@RequestBody User user) {
        userService.saveNewAdmin(user);
    }

    @GetMapping("clear-app-cache")
    public void clearAppCache() {
        appcache.init() ;
    }
}
