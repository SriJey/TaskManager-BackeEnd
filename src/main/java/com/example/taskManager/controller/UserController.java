package com.example.taskManager.controller;

import com.example.taskManager.entity.User;
import com.example.taskManager.exceptions.UserNotFoundException;
import com.example.taskManager.model.Authentication;
import com.example.taskManager.model.AuthenticationResponse;
import com.example.taskManager.model.BasicUserDetails;
import com.example.taskManager.model.RegisterResponse;
import com.example.taskManager.model.UserLogin;
import com.example.taskManager.service.UserService;
import com.example.taskManager.utility.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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
    public RegisterResponse register(@RequestBody UserLogin user){
        try{
            return userService.addUser(user);
        }
        catch (Exception e){
            return new RegisterResponse(Constant.ERROR,e.getMessage(),Constant.NULL);
        }

    }
    @GetMapping(Constant.GET_DETAILS)
    public List<BasicUserDetails> getBasicDetails(){
        return userService.getBasicDetails();
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
    public AuthenticationResponse login(@RequestBody Authentication authentication){
        try{
            return userService.userAuthentication(authentication);
        }
        catch (Exception e){
            return new AuthenticationResponse(Constant.ERROR,e.getLocalizedMessage(),Constant.NULL);
        }
    }

    @GetMapping(Constant.REDIS_ALL_ELEMENT)
    public Map<String,String> redisGetAll(){
        try {
            return userService.redisGetAll();
        }
        catch (Exception exception){
            return new HashMap<>();
        }
    }


    @PutMapping(Constant.CHANGE_STATUS)
    public boolean changeUserStatus(@PathVariable String email){
        try {
            return userService.changeStatus(email);
        }
        catch (Exception exception){
            return false;
        }
    }

    @GetMapping("get/id/{id}")
    public BasicUserDetails getBasicDetailsById(@PathVariable String id){
        try{
            return userService.getUsersById(id);
        }
        catch (UserNotFoundException exception){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,exception.getMessage());
        }
        catch (Exception exception){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Something went Wrong");
        }
    }

}
