package com.dkdev.Journal.Application.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.cglib.core.Local;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;

@Document
@Data
public class JournalEntry {

    @Id
    private ObjectId id ;
    @NonNull
    private String title ;
    private String content ;
    private LocalDateTime date ;
}
