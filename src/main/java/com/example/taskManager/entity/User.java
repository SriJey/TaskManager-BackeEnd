package com.example.taskManager.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    private String id;
    private String name;

    private String role;
    private String designation;
    private String password;
    @CreationTimestamp
    private Date dateOfJoining;
    private String email;
    private boolean status;
//    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
//    private List<Task> task;
//    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
//    private List<TaskDetails> taskDetails;
}
