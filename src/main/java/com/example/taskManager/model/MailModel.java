package com.example.taskManager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MailModel {
    private String recieverMail;
    private String message;
    private String subject;
}
