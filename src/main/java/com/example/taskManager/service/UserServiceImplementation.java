package com.example.taskManager.service;

import com.example.taskManager.entity.User;
import com.example.taskManager.exceptions.UserNotFoundException;
import com.example.taskManager.model.Authentication;
import com.example.taskManager.model.AuthenticationResponse;
import com.example.taskManager.model.BasicUserDetails;
import com.example.taskManager.model.MailModel;
import com.example.taskManager.model.PasswordChange;
import com.example.taskManager.model.RegisterResponse;
import com.example.taskManager.model.UserBasicLogin;
import com.example.taskManager.model.UserLogin;
import com.example.taskManager.repository.UserRepo;
import com.example.taskManager.utility.Constant;
import com.example.taskManager.utility.Validation;
import com.example.taskManager.utility.hashPassword;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.example.taskManager.utility.hashPassword.encryptThisString;


@Service
public class UserServiceImplementation implements  UserService{

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    UserRepo userRepo;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public RegisterResponse addUser(UserLogin userLogin) throws Exception{
        Validation.validateName(userLogin.getName());
        Validation.validateEmail(userLogin.getEmail());
        if(userRepo.findByEmail(userLogin.getEmail())!=null){
            return new RegisterResponse(Constant.ERROR,Constant.EMAIL_ALREADY_EXISTS,Constant.NULL);
        }
        User user = modelMapper.map(userLogin,User.class);
        user.setId(UUID.randomUUID().toString());
        user.setEmail(userLogin.getEmail().toLowerCase());
        user.setName(userLogin.getName().trim());
        String generatedPassword = UUID.randomUUID().toString();
        user.setPassword(hashPassword.encryptThisString(generatedPassword));

        // To trigger Mail REst API

        MailModel mailModel = new MailModel();
        mailModel.setMessage(Constant.MAIL_NEW_REGISTER_MESSAGE+generatedPassword);
        mailModel.setRecieverMail(user.getEmail());
        mailModel.setSubject(Constant.MAIL_NEW_REGISTER_SUBJECT);
        mailUser(mailModel);

        user.setStatus(false);
        user.setRole(Constant.USER_ROLE);
        userRepo.save(user);
        UserBasicLogin userBasicLogin = modelMapper.map(user,UserBasicLogin.class);
        String payload = objectMapper.writeValueAsString(userBasicLogin);
        redisTemplate.opsForHash().put(Constant.REDIS_EMAIL_PASSWORD,user.getEmail(),payload);
        return new RegisterResponse(Constant.OK,Constant.SUCCESS,user.getId());
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
            redisTemplate.delete(key);
        }
        return Constant.SUCCESS;
    }

    @Override
    public AuthenticationResponse userAuthentication(Authentication authentication) throws JsonProcessingException,Exception {
        String email = authentication.getEmail();
        String password = hashPassword.encryptThisString(authentication.getPassword());

        if(redisTemplate.opsForHash().hasKey(Constant.REDIS_EMAIL_PASSWORD,email)){ //If present in Cache
            String payload = (String) redisTemplate.opsForHash().get(Constant.REDIS_EMAIL_PASSWORD,email);
            UserBasicLogin userBasicLogin = objectMapper.readValue(payload,UserBasicLogin.class);
            if(userBasicLogin.getPassword().equals(password)){
                return new AuthenticationResponse(Constant.OK,Constant.EMAIL_PASSWORD_MATCH,userBasicLogin.getRole());
            }
            else{
                // Incorrect Email
                if(redisTemplate.hasKey(email)){
                    int count = Integer.parseInt((String)redisTemplate.opsForValue().get(email));
                    if(count==3){
                        changeStatusToFalse(email);
                        MailModel mailModel = new MailModel(email,null,null);
                        mailUser(mailModel);
                        return new AuthenticationResponse(Constant.ERROR,"ID-BLOCKED",Constant.NULL);
                    }
                    redisTemplate.opsForValue().set(email,String.valueOf(++count), 3600,TimeUnit.SECONDS);
                }
                else{
                    redisTemplate.opsForValue().set(email,"1");
                }
                return new AuthenticationResponse(Constant.ERROR,Constant.INCORRECT_PASSWORD,Constant.NULL);
            }
        }
        else{ //If not present in Cache check in DB
            User user = userRepo.findByEmail(email);
            if(user!=null){//mail exists in db
                if(user.getPassword().equals(password)){ //if such combination is present
                    // populate in Redis
                    UserBasicLogin userBasicLogin = modelMapper.map(user,UserBasicLogin.class);
                    String payload = objectMapper.writeValueAsString(userBasicLogin);
                    redisTemplate.opsForHash().put(Constant.REDIS_EMAIL_PASSWORD,email,payload);
                    return new AuthenticationResponse(Constant.OK,Constant.EMAIL_PASSWORD_MATCH,userBasicLogin.getRole());
                }
                else {
                    if(redisTemplate.hasKey(email)){
                        int count = Integer.parseInt((String)redisTemplate.opsForValue().get(email));
                        if(count>=3){
                            changeStatusToFalse(email);
                            MailModel mailModel = new MailModel(email,null,null);
                            mailUser(mailModel);
                            return new AuthenticationResponse(Constant.ERROR,"ID-BLOCKED",Constant.NULL);
                        }
                        redisTemplate.opsForValue().set(email,String.valueOf(++count), 3600,TimeUnit.SECONDS);
                    }
                    else{
                        redisTemplate.opsForValue().set(email,"1");
                    }
                    return new AuthenticationResponse(Constant.ERROR,Constant.INCORRECT_PASSWORD,Constant.NULL);
                }
            }
            else {
                return new AuthenticationResponse(Constant.ERROR,Constant.EMAIL_NOT_EXISTS,Constant.NULL);
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
            redisTemplate.opsForValue().set(email,"0");
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

    @Override
    public String changePassword(PasswordChange passwordChange) {
        Optional<User> user = userRepo.findById(passwordChange.getId());
        if(user!=null){
            user.get().setPassword(hashPassword.encryptThisString(passwordChange.getPassword()) );
            userRepo.save(user.get());
            return Constant.SUCCESS;
        }
        return Constant.ERROR;
    }

    public void changeStatusToFalse(String email){
        User user = userRepo.findByEmail(email);
        if(user!=null){
            user.setStatus(false);
            userRepo.save(user);
        }

    }

    public void mailUser(MailModel mailModel) throws Exception{
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<MailModel> entity = new HttpEntity<MailModel>(mailModel,headers);
        restTemplate.exchange(Constant.REST_TEMPLATE_URL, HttpMethod.POST,
                entity, String.class).getBody();

    }
}
