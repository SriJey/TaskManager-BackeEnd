package com.example.taskManager.utility;

import com.example.taskManager.exceptions.EmailException;
import com.example.taskManager.exceptions.NameException;
import com.example.taskManager.exceptions.PasswordException;
import com.example.taskManager.exceptions.RoleException;

public class Validation {
    public static String validateName(String name) throws Exception{
        name = name.trim();
        if(name.isEmpty()){
            throw new NameException("Name cannot be empty.");
        }
        if(name.length()<=2){
            throw new NameException("Length of the name must be greater than 2 Characters at least.");
        }
        if(name.length()>32){
            throw new NameException("Length of the name must be lesser than 32 Characters.");
        }

        if(!name.matches("^[A-Za-z][A-Za-z ]{2,31}$")){
            throw new NameException("Invalid Name Input.");
        }
        return Constant.SUCCESS;
    }

    public static String validateEmail(String email) throws Exception{
        email=email.trim();
        if(email.isEmpty()){
            throw new EmailException("Email cannot be Empty");
        }
        if(!email.matches(Constant.QUINBAY_EMAIL_REGEX)){
            throw new EmailException("Not a valid Email");
        }
        return Constant.SUCCESS;
    }
    public static String validateRole(String role)throws Exception{
        if(role.isEmpty()){
            throw new RoleException("Role cannot be Empty");
        }
        if(role.equalsIgnoreCase("admin")||role.equalsIgnoreCase("intern")){
            return Constant.SUCCESS;
        }
        throw new RoleException("Role should either be an Intern or an Admin");
    }
    public static String validatePassword(String password)throws Exception{
        String regex = Constant.PASSWORD_REGEX;
        if(password.matches(regex)) {
            return Constant.SUCCESS;
        }
        throw new PasswordException( Constant.PASSWORD_PASSAGE);
    }
}
