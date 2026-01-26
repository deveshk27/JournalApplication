package com.dkdev.Journal.Application.scheduler;

import com.dkdev.Journal.Application.cache.Appcache;
import com.dkdev.Journal.Application.entity.JournalEntry;
import com.dkdev.Journal.Application.entity.User;
import com.dkdev.Journal.Application.enums.Sentiment;
import com.dkdev.Journal.Application.model.SentimentData;
import com.dkdev.Journal.Application.repository.UserRepositoryImpl;
import com.dkdev.Journal.Application.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserScheduler {

    @Autowired
    private UserRepositoryImpl userRepository ;

    @Autowired
    private EmailService emailService ;

    @Autowired
    private Appcache appcache ;

    @Autowired
    private KafkaTemplate<String, SentimentData> kafkaTemplate ;

    public void fetchUserAndSendEmail() {
        List<User> users = userRepository.getUserForSA();
        for(User user : users) {
            List<JournalEntry> journalEntries = user.getJournalEntries() ;
            List<Sentiment> sentiments = journalEntries.stream().filter(x -> x.getDate().isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS))).map(x -> x.getSentiment()).collect(Collectors.toList());
            Map<Sentiment, Integer> sentimentCounts = new HashMap<>();
            for (Sentiment sentiment : sentiments) {
                if (sentiment != null)
                    sentimentCounts.put(sentiment, sentimentCounts.getOrDefault(sentiment, 0) + 1);
            }
            Sentiment mostFrequentSentiment = null;
            int maxCount = 0;
            for (Map.Entry<Sentiment, Integer> entry : sentimentCounts.entrySet()) {
                if (entry.getValue() > maxCount) {
                    maxCount = entry.getValue();
                    mostFrequentSentiment = entry.getKey();
                }
            }

            if(mostFrequentSentiment != null) {
                SentimentData sentimentData = SentimentData.builder().email(user.getEmail()).sentiment("Sentiment for last 7 days " + mostFrequentSentiment).build();
                try{
                    kafkaTemplate.send("journalApp", sentimentData.getEmail(), sentimentData);
                }catch (Exception e){
                    emailService.sendEmail(sentimentData.getEmail(), "Sentiment for previous week", sentimentData.getSentiment());
                }
//              emailService.sendEmail(user.getEmail(), "Sentiment for last 7 days" , mostFrequentSentiment.toString());
            }
        }
    }

    @Scheduled(cron = "0 */10 * * * *")
    public void clearAppCache() {
        appcache.init();
    }
}
