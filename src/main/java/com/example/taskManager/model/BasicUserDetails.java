package com.example.taskManager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasicUserDetails {
    private String id;
    private String name;
    private String role;
    private String designation;
    private Date dateOfJoining;
    private String email;
    private boolean status;
}
