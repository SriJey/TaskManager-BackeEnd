package com.example.taskManager.controller;

import com.example.taskManager.entity.User;
import com.example.taskManager.model.Authentication;
import com.example.taskManager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/register")
    public String register(@RequestBody User user){
        try{
            return userService.addUser(user);
        }
        catch (Exception e){
            return e.getMessage();
        }

    }
    @GetMapping("/allusers")
    public List<User> getAllUsers(){
        return userService.getAllUser();
    }
    @DeleteMapping("/delete/all")
    public String deleteAllUsers(){
        return userService.deleteAllUsers();
    }

//    @PostMapping("/login")
//    public String login(Authentication authentication){
//
//    }
}
