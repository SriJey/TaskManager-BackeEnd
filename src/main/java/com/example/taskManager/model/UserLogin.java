package com.example.taskManager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLogin {
    private String name;
    private String designation;
    private String password;
    private String email;
}
