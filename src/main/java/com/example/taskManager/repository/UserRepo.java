package com.example.taskManager.repository;

import com.example.taskManager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface UserRepo extends JpaRepository<User,Integer> {
    User findByEmail(String email);
    User findByEmailAndPassword(String email,String password);
}
