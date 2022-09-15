package com.example.taskManager.utility;

import com.example.taskManager.exceptions.EmailException;
import com.example.taskManager.exceptions.NameException;
import com.example.taskManager.exceptions.PasswordException;
import com.example.taskManager.model.AuthenticationResponse;
import com.example.taskManager.model.RegisterResponse;

public class Validation {
    public static RegisterResponse validateName(String name) throws Exception{
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
        return new RegisterResponse(Constant.OK, Constant.SUCCESS,Constant.NULL);
    }

    public static RegisterResponse validateEmail(String email) throws Exception{
        email=email.trim();
        if(email.isEmpty()){
            throw new EmailException(Constant.EMAIL_EMPTY);
        }
        if(!email.matches(Constant.QUINBAY_EMAIL_REGEX)){
            throw new EmailException(Constant.NOT_A_VALID_EMAIL);
        }
        return new RegisterResponse(Constant.OK, Constant.SUCCESS,Constant.NULL);
    }



    public static RegisterResponse  validatePassword(String password)throws Exception{
        String regex = Constant.PASSWORD_REGEX;
        if(password.matches(regex)) {
            return new RegisterResponse(Constant.OK,Constant.SUCCESS,Constant.NULL);
        }
        throw new PasswordException( Constant.PASSWORD_PASSAGE);
    }
}
