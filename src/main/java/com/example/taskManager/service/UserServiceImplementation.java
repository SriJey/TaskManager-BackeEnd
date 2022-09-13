package com.example.taskManager.service;

import com.example.taskManager.entity.User;
import com.example.taskManager.model.Authentication;
import com.example.taskManager.repository.UserRepo;
import com.example.taskManager.utility.Constant;
import com.example.taskManager.utility.Validation;
import com.example.taskManager.utility.hashPassword;
import com.sun.tools.classfile.ConstantPool;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.taskManager.utility.hashPassword.encryptThisString;


@Service
public class UserServiceImplementation implements  UserService{
    @Autowired
    UserRepo userRepo;
    @Override
    public String addUser(User user) throws Exception{
        user.setName(user.getName().trim());
        Validation.validateName(user.getName());
        Validation.validateEmail(user.getEmail());
        Validation.validatePassword(user.getPassword());
        user.setPassword(encryptThisString(user.getPassword()));
        userRepo.save(user);
        return Constant.SUCCESS;
    }

    @Override
    public List<User> getAllUser() {
        return userRepo.findAll();
    }

    @Override
    public String deleteAllUsers() {
        userRepo.deleteAll();
        return Constant.SUCCESS;
    }

    @Override
    public String userAuthentication(Authentication authentication) throws Exception {
        System.out.println("Hello 01");
        String email = authentication.getEmail();
        System.out.println("Hello 02");
        String password = hashPassword.encryptThisString(authentication.getPassword());
        System.out.println("Hello 1");
        if( userRepo.findByEmail(email)!=null){
            if(userRepo.findByEmailAndPassword(email,password)!=null){
                return "Password and Email id Matches";
            }
            else{
                return "Incorrect Password";
            }
        }
        System.out.println("Hello 2");

        return "Email doesnot exits";
    }
}
