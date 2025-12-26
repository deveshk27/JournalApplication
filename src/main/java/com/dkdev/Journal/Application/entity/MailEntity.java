package com.dkdev.Journal.Application.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class MailEntity {
    private String to ;
    private String subject ;
    private String body ;
}
