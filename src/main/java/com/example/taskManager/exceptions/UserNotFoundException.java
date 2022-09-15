package com.example.taskManager.exceptions;

public class UserNotFoundException extends  Exception{
    public UserNotFoundException(String message){
        super(message);
    }
}
