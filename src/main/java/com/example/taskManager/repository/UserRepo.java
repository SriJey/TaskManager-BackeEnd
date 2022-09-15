package com.example.taskManager.repository;

import com.example.taskManager.entity.User;
import com.example.taskManager.model.BasicUserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRepo extends JpaRepository<User,String> {
    User findByEmail(String email);
    User findByEmailAndPassword(String email,String password);
    List<User> findAllByStatusTrue();
}
