package com.dkdev.Journal.Application.repository;

import com.dkdev.Journal.Application.entity.JournalEntry;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JournelEntryRepository extends MongoRepository<JournalEntry, ObjectId> {

}
