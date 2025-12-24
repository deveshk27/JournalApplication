 package com.dkdev.Journal.Application.controller;

import com.dkdev.Journal.Application.entity.JournalEntry;
import com.dkdev.Journal.Application.entity.User;
import com.dkdev.Journal.Application.service.JournelEntryService;
import com.dkdev.Journal.Application.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Repeatable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
public class JournalEntryControllerV2 {

    @Autowired
    private JournelEntryService journelEntryService ;

    @Autowired
    private UserService userService ;

    @GetMapping
    public ResponseEntity<?> getAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName() ;
        User user = userService.findByUserName(username) ;
        List<JournalEntry> all = user.getJournalEntries() ;
        if(all != null && !all.isEmpty()) {
            return new ResponseEntity<>(all , HttpStatus.OK) ;
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND) ;
    }

    @PostMapping
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry) {
       try {
           Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
           String username = authentication.getName();
           journelEntryService.saveJournel(myEntry,username);
           return new ResponseEntity<JournalEntry>(myEntry , HttpStatus.CREATED) ;
       } catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST) ;
       }
    }

    @GetMapping("id/{myId}")
    public ResponseEntity<JournalEntry> getEntryById(@PathVariable ObjectId myId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName() ;
        User user = userService.findByUserName(username) ;
        List<JournalEntry> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(myId)).collect(Collectors.toList()) ;
        if(!collect.isEmpty()) {
            Optional<JournalEntry> journalEntry = journelEntryService.findById(myId) ;
            if(journalEntry.isPresent()) {
                return new ResponseEntity<JournalEntry>(journalEntry.get() , HttpStatus.OK) ;
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND) ;
    }

    @DeleteMapping("id/{myId}")
    public ResponseEntity<?> deleteJournalById(@PathVariable ObjectId myId) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName() ;
            boolean removed = journelEntryService.deleteById(myId,username);
            if(removed) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT) ;
            }
            else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND) ;
            }
    }

    @PutMapping("id/{myId}")
    public ResponseEntity<?> putJournalEntryById(@PathVariable ObjectId myId , @RequestBody JournalEntry newEntry) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName() ;
        User user = userService.findByUserName(username) ;
        List<JournalEntry> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(myId)).collect(Collectors.toList()) ;
        if(!collect.isEmpty()) {
            Optional<JournalEntry> journalEntry = journelEntryService.findById(myId) ;
            if(journalEntry.isPresent()) {
                JournalEntry oldEntry = journalEntry.get() ;
                oldEntry.setTitle(newEntry.getTitle() != null && !Objects.equals(newEntry.getTitle(), "") ? newEntry.getTitle() : oldEntry.getTitle());
                oldEntry.setContent(newEntry.getContent() != null && !Objects.equals(newEntry.getContent(),"") ? newEntry.getContent() : oldEntry.getContent()) ;
                journelEntryService.saveJournel(oldEntry);
                return new ResponseEntity<>(oldEntry, HttpStatus.OK) ;
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND) ;
    }
}
