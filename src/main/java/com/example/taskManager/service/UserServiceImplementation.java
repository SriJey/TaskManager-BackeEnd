package com.example.taskManager.service;

import com.example.taskManager.entity.User;
import com.example.taskManager.exceptions.UserNotFoundException;
import com.example.taskManager.model.Authentication;
import com.example.taskManager.model.AuthenticationResponse;
import com.example.taskManager.model.BasicUserDetails;
import com.example.taskManager.model.UserLogin;
import com.example.taskManager.repository.UserRepo;
import com.example.taskManager.utility.Constant;
import com.example.taskManager.utility.Validation;
import com.example.taskManager.utility.hashPassword;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static com.example.taskManager.utility.hashPassword.encryptThisString;


@Service
public class UserServiceImplementation implements  UserService{

    @Autowired
    UserRepo userRepo;

    @Autowired
    RedisTemplate redisTemplate;



    @Autowired
    ModelMapper modelMapper;


    @Override
    public AuthenticationResponse addUser(UserLogin userLogin) throws Exception{
        Validation.validateName(userLogin.getName());
        Validation.validateEmail(userLogin.getEmail());
        Validation.validatePassword(userLogin.getPassword());
        if(userRepo.findByEmail(userLogin.getEmail())!=null){
            return new AuthenticationResponse(Constant.ERROR,Constant.EMAIL_ALREADY_EXISTS);
        }
        User user = modelMapper.map(userLogin,User.class);
        user.setId(UUID.randomUUID().toString());
        user.setEmail(userLogin.getEmail().toLowerCase());
        user.setName(userLogin.getName().trim());
        user.setPassword(encryptThisString(userLogin.getPassword()));
        user.setStatus(false);
        user.setRole(Constant.USER_ROLE);
        userRepo.save(user);
        redisTemplate.opsForHash().put(Constant.REDIS_EMAIL_PASSWORD,user.getEmail(),user.getPassword());
        return new AuthenticationResponse(Constant.OK,Constant.SUCCESS);
    }


    @Override
    public List<User> getAllUser() {
        return userRepo.findAll();
    }

    @Override
    public String deleteAllUsers() {
        userRepo.deleteAll();
        Set<String> keys = redisTemplate.opsForHash().keys(Constant.REDIS_EMAIL_PASSWORD);
        for (String key : keys) {
            redisTemplate.opsForHash().delete(Constant.REDIS_EMAIL_PASSWORD,key);
        }
        return Constant.SUCCESS;
    }

    @Override
    public AuthenticationResponse userAuthentication(Authentication authentication){
        String email = authentication.getEmail();
        String password = hashPassword.encryptThisString(authentication.getPassword());

        if(redisTemplate.opsForHash().hasKey(Constant.REDIS_EMAIL_PASSWORD,email)){ //If present in Cache
//            LoginResponseDetails loginResponseDetails = (LoginResponseDetails) redisTemplate.opsForHash().get(Constant.REDIS_EMAIL_PASSWORD,email);
            String retrievedPassword = (String) redisTemplate.opsForHash().get(Constant.REDIS_EMAIL_PASSWORD,email);
            if(retrievedPassword.equals(password)){
                return new AuthenticationResponse(Constant.OK,Constant.EMAIL_PASSWORD_MATCH);
            }
            else{
                // New Redis Implementation
                return new AuthenticationResponse(Constant.ERROR,Constant.INCORRECT_PASSWORD);
            }
        }
        else{ //If not present in Cache check in DB
            User user = userRepo.findByEmail(email);
            if(user!=null){//mail exists in db
                if(user.getPassword().equals(password)){ //if such combination is present
                    // populate in Redis
                    redisTemplate.opsForHash().put(Constant.REDIS_EMAIL_PASSWORD,email,password);
                    return new AuthenticationResponse(Constant.OK,Constant.EMAIL_PASSWORD_MATCH);
                }
                else {
                    //New Reddis Implementation
                    return new AuthenticationResponse(Constant.ERROR,Constant.INCORRECT_PASSWORD);
                }
            }
            else {
                return new AuthenticationResponse(Constant.ERROR,Constant.EMAIL_NOT_EXISTS);
            }
        }
    }

    @Override
    public Map<String, String> redisGetAll() {
        return redisTemplate.opsForHash().entries(Constant.REDIS_EMAIL_PASSWORD);
    }



    @Override
    public boolean changeStatus(String email) {
        User user = userRepo.findByEmail(email.toLowerCase());
        if(user!=null){
            user.setStatus(true);
            userRepo.save(user);
            return true;
        }
        return false;
    }

    @Override
    public List<BasicUserDetails> getBasicDetails() {
        List<BasicUserDetails> list = new ArrayList<>();
        List<User> userList = userRepo.findAllByStatusTrue();

        for(User user:userList){
            BasicUserDetails basicUserDetails = new BasicUserDetails();
            basicUserDetails = modelMapper.map(user,BasicUserDetails.class);
            list.add(basicUserDetails);
        }
        return list;
    }

    @Override
    public BasicUserDetails getUsersById(String id) throws UserNotFoundException{
        User user = userRepo.findById(id).get();
        if(user==null){
            throw new UserNotFoundException("User Not Found");
        }
        BasicUserDetails basicUserDetails = modelMapper.map(user,BasicUserDetails.class);
        return basicUserDetails;
    }
}
