package com.dkdev.Journal.Application.scheduler;

import com.dkdev.Journal.Application.cache.Appcache;
import com.dkdev.Journal.Application.entity.JournalEntry;
import com.dkdev.Journal.Application.entity.User;
import com.dkdev.Journal.Application.repository.UserRepositoryImpl;
import com.dkdev.Journal.Application.service.EmailService;
import com.dkdev.Journal.Application.service.SentimentAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Component
public class UserScheduler {

    @Autowired
    private UserRepositoryImpl userRepository ;

    @Autowired
    private EmailService emailService ;

    @Autowired
    private SentimentAnalysisService sentimentAnalysisService ;

    @Autowired
    private Appcache appcache ;

    @Scheduled(cron = "0 0 9 * * SUN")
    public void fetchUserAndSendEmail() {
        List<User> users = userRepository.getUserForSA();
        for(User user : users) {
            List<JournalEntry> journalEntries = user.getJournalEntries() ;
            List<String> filteredEntries = journalEntries.stream().filter(x -> x.getDate().isAfter(LocalDateTime.now().minus(7 , ChronoUnit.DAYS))).map(x -> x.getContent()).collect(Collectors.toList()) ;
            String entry = String.join(" " , filteredEntries) ;
            String sentiment = sentimentAnalysisService.getSentiment(entry) ;
            emailService.sendEmail(user.getEmail() , "Sentiment for the last 7 days" , sentiment);
        }
    }

    @Scheduled(cron = "0 */10 * * * *")
    public void clearAppCache() {
        appcache.init();
    }
}
