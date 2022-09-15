package com.example.taskManager.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserBasicLogin {
    private String password;
    private String role;
}
