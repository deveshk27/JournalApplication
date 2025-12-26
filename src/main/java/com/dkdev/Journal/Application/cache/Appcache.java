package com.dkdev.Journal.Application.cache;

import com.dkdev.Journal.Application.entity.ConfigJournalAppEntity;
import com.dkdev.Journal.Application.repository.ConfigJournalAppRepository;
import jakarta.annotation.PostConstruct;
import org.apache.catalina.core.ApplicationFilterChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Appcache {
    public enum keys {
        WEATHER_API
    }

    @Autowired
    private ConfigJournalAppRepository configJournalAppRepository ;

    public Map<String,String> AppCache ;

    @PostConstruct
    public void init() {
        AppCache = new HashMap<>() ;
        List<ConfigJournalAppEntity> all = configJournalAppRepository.findAll() ;
        for(ConfigJournalAppEntity configJournalAppEntity : all) {
            AppCache.put(configJournalAppEntity.getKey() , configJournalAppEntity.getValue()) ;
        }
    }
}
