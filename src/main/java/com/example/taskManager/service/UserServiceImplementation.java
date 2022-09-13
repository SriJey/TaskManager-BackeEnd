package com.example.taskManager.service;

import com.example.taskManager.entity.User;
import com.example.taskManager.model.Authentication;
import com.example.taskManager.repository.UserRepo;
import com.example.taskManager.utility.Constant;
import com.example.taskManager.utility.Validation;
import com.example.taskManager.utility.hashPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.example.taskManager.utility.hashPassword.encryptThisString;


@Service
public class UserServiceImplementation implements  UserService{

    @Autowired
    UserRepo userRepo;

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public String addUser(User user) throws Exception{
        user.setName(user.getName().trim());
        Validation.validateName(user.getName());
        Validation.validateEmail(user.getEmail());
        if(userRepo.findByEmail(user.getEmail())!=null){
            return Constant.EMAIL_ALREADY_EXISTS;
        }
        Validation.validatePassword(user.getPassword());
        Validation.validateRole(user.getRole());
        user.setPassword(encryptThisString(user.getPassword()));
        userRepo.save(user);
        redisTemplate.opsForHash().put(Constant.REDIS_MASTER_KEY,user.getEmail(),user.getPassword());
        return Constant.SUCCESS;
    }


    @Override
    public List<User> getAllUser() {
        return userRepo.findAll();
    }

    @Override
    public String deleteAllUsers() {
        userRepo.deleteAll();
        Set<String> keys = redisTemplate.opsForHash().keys(Constant.REDIS_MASTER_KEY);
        for (String key : keys) {
            redisTemplate.opsForHash().delete(Constant.REDIS_MASTER_KEY,key);
        }
        return Constant.SUCCESS;
    }

    @Override
    public String userAuthentication(Authentication authentication){
        String email = authentication.getEmail();
        String password = hashPassword.encryptThisString(authentication.getPassword());

        if(redisTemplate.opsForHash().hasKey(Constant.REDIS_MASTER_KEY,email)){ //If present in Cache
            String retrievedPassword = (String)redisTemplate.opsForHash().get(Constant.REDIS_MASTER_KEY,email);
            if(retrievedPassword.equals(password)){
                return Constant.EMAIL_PASSWORD_MATCH; // CORRECT PASSWORD
            }
            else{
                return Constant.INCORRECT_PASSWORD; //Incorrect Password
            }
        }
        else{ //If not present in Cache check in DB
            if(userRepo.findByEmail(email)!=null){//mail exists in db
                if(userRepo.findByEmailAndPassword(email,password)!=null){ //if such combination is present
                    redisTemplate.opsForHash().put(Constant.REDIS_MASTER_KEY,email,password);//populate in Redis
                    return Constant.EMAIL_PASSWORD_MATCH; //return password matches
                }
                else {
                    return Constant.INCORRECT_PASSWORD;//email found but password not matches
                }
            }
            else {
                return Constant.EMAIL_NOT_EXISTS;//email id does not exists
            }
        }
    }

    @Override
    public Map<String, String> redisGetAll() {
        return redisTemplate.opsForHash().entries(Constant.REDIS_MASTER_KEY);
    }
}
