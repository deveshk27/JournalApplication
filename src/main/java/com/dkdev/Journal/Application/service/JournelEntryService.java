package com.dkdev.Journal.Application.service;

import com.dkdev.Journal.Application.entity.JournalEntry;
import com.dkdev.Journal.Application.entity.User;
import com.dkdev.Journal.Application.repository.JournelEntryRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class JournelEntryService {

    @Autowired
    private JournelEntryRepository journelEntryRepository ;
    @Autowired
    private UserService userService;

    @Transactional
    public void saveJournel(JournalEntry journalEntry, String username) {
        try {
            User user = userService.findByUserName(username) ;
            journalEntry.setDate(LocalDateTime.now());
            JournalEntry saved = journelEntryRepository.save(journalEntry);
            user.getJournalEntries().add(saved) ;
            userService.saveUser(user);
        } catch(Exception e) {
            log.error("Error" , e);
            throw new RuntimeException("An error occurred while saving the journal." , e) ;
        }
    }

    public void saveJournel(JournalEntry journalEntry) {
        journelEntryRepository.save(journalEntry) ;
    }

    public List<JournalEntry> getAll() {
        return journelEntryRepository.findAll() ;
    }

    public Optional<JournalEntry> findById(ObjectId id) {
        return journelEntryRepository.findById(id) ;
    }

    @Transactional
    public boolean deleteById(ObjectId id, String username) {
        boolean removed = false ;
        try {
            User user = userService.findByUserName(username) ;
            removed = user.getJournalEntries().removeIf(x -> x.getId().equals(id)) ;
            if(removed) {
                userService.saveUser(user);
                journelEntryRepository.deleteById(id);
            }
        } catch(Exception e){
            System.out.println(e) ;
            throw new RuntimeException("An error occured while deleting the journal." , e) ;
        }
        return removed ;
    }
}
