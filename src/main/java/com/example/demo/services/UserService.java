package com.example.demo.services;

import com.example.demo.models.Login;
import com.example.demo.repositories.UserRepository;
import com.example.demo.models.User;
import com.example.demo.security.SHA256;
import com.example.demo.security.TokenAuthentication;
import com.example.demo.utils.RandomAlphaNumericString;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    private RandomAlphaNumericString randomAlphaNumericString;
    @Autowired
    private TokenAuthentication tokenAuthentication;

    public User login(Login login) throws NotFoundException, NoSuchAlgorithmException {
        User user = userRepository.findByUsername(login.getUsername());
        System.out.println(login.getPassword());
        if (user == null){
            throw new NotFoundException("username or password is not correct");
        }
        else{
            if (user.checkPassword(login.getPassword())){
                return user;
            }
            else{
                throw new NotFoundException("username or password is not correct");
            }
        }
    }

    public User register(User newUser){
        tokenAuthentication.generateNewUserToken(newUser);
        newUser.setPasswordSalt(randomAlphaNumericString.randomAlphaNumeric(10));
        try {
            String hashedPassword = SHA256.makeHash(newUser.getPassword(), newUser.getPasswordSalt().getBytes());
            newUser.setPassword(hashedPassword);
            userRepository.save(newUser);
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return newUser;
    }
}
