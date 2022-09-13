package com.example.taskManager.controller;

import com.example.taskManager.entity.User;
import com.example.taskManager.model.Authentication;
import com.example.taskManager.service.UserService;
import com.example.taskManager.utility.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(Constant.USER)
@CrossOrigin(Constant.CROSS_ORIGIN)
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping(Constant.REGISTER)
    public String register(@RequestBody User user){
        try{
            return userService.addUser(user);
        }
        catch (Exception e){
            return e.getMessage();
        }

    }

    @GetMapping(Constant.ALL_USER)
    public List<User> getAllUsers(){
        return userService.getAllUser();
    }

    @DeleteMapping(Constant.DELETE_ALL)
    public String deleteAllUsers(){
        return userService.deleteAllUsers();
    }

    @PostMapping(Constant.LOGIN)
    public String login(@RequestBody Authentication authentication){
        try{
            return userService.userAuthentication(authentication);
        }
        catch (Exception e){
            return e.getLocalizedMessage();
        }
    }

    @GetMapping(Constant.REDIS_ALL_ELEMENT)
    public Map<String,String> redisGetAll(){
        try {
            return userService.redisGetAll();
        }
        catch (Exception e){
            return new HashMap<>();
        }
    }

}
