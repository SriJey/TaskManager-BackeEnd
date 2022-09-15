package com.example.taskManager.service;


import com.example.taskManager.entity.User;
import com.example.taskManager.exceptions.UserNotFoundException;
import com.example.taskManager.model.Authentication;
import com.example.taskManager.model.AuthenticationResponse;
import com.example.taskManager.model.BasicUserDetails;
import com.example.taskManager.model.PasswordChange;
import com.example.taskManager.model.RegisterResponse;
import com.example.taskManager.model.UserLogin;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserService {
    RegisterResponse addUser(UserLogin user) throws Exception;
    List<User> getAllUser();
    String deleteAllUsers();
    AuthenticationResponse userAuthentication(Authentication authentication) throws Exception;
    Map<String,String> redisGetAll();
    boolean changeStatus(String email);
    List<BasicUserDetails> getBasicDetails();
    BasicUserDetails getUsersById(String id) throws UserNotFoundException;
    String changePassword(PasswordChange passwordChange);
}