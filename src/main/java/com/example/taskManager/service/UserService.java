package com.example.taskManager.service;


import com.example.taskManager.entity.User;
import com.example.taskManager.model.Authentication;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserService {
    public String addUser(User user) throws Exception;
    public List<User> getAllUser();
    public String deleteAllUsers();
    public String userAuthentication(Authentication authentication) throws Exception;
    public Map<String,String> redisGetAll();

}
