package com.example.taskManager.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private String role;
    private String password;
    @CreationTimestamp
    private Date dateOfJoining;
    private String email;
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Task> task;
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<TaskDetails> taskDetails;
}
