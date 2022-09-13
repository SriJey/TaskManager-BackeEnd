package com.example.taskManager.utility;

import com.example.taskManager.exceptions.EmailException;
import com.example.taskManager.exceptions.NameException;
import com.example.taskManager.exceptions.PasswordException;
import com.example.taskManager.exceptions.RoleException;

public class Validation {
    public static String validateName(String name) throws Exception{
        name = name.trim();
        if(name.isEmpty()){
            throw new NameException(Constant.NAME_EMPTY);
        }
        if(name.length()<=Constant.NAME_MIN_LENGTH){
            throw new NameException(Constant.NAME_MINIMUM_LENGTH);
        }
        if(name.length()>Constant.NAME_MAX_LENGTH){
            throw new NameException(Constant.Name_MAXIMUM_LENGTH);
        }

        if(!name.matches(Constant.NAME_REGEX)){
            throw new NameException(Constant.NAME_INVALID_INPUT);
        }
        return Constant.SUCCESS;
    }

    public static String validateEmail(String email) throws Exception{
        email=email.trim();
        if(email.isEmpty()){
            throw new EmailException(Constant.EMAIL_EMPTY);
        }
        if(!email.matches(Constant.QUINBAY_EMAIL_REGEX)){
            throw new EmailException(Constant.NOT_A_VALID_EMAIL);
        }
        return Constant.SUCCESS;
    }
    public static String validateRole(String role)throws Exception{
        if(role.isEmpty()){
            throw new RoleException(Constant.ROLE_EMPTY);
        }
        if(role.equalsIgnoreCase(Constant.ADMIN)||role.equalsIgnoreCase(Constant.INTERN)){
            return Constant.SUCCESS;
        }
        throw new RoleException(Constant.ROLE_EXCEPTION);
    }
    public static String validatePassword(String password)throws Exception{
        String regex = Constant.PASSWORD_REGEX;
        if(password.matches(regex)) {
            return Constant.SUCCESS;
        }
        throw new PasswordException( Constant.PASSWORD_PASSAGE);
    }
}
