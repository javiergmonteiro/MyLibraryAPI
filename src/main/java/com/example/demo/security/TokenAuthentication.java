package com.example.demo.security;

import com.example.demo.repositories.UserRepository;
import com.example.demo.models.User;
import com.example.demo.utils.RandomAlphaNumericString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenAuthentication {

    @Autowired
    private UserRepository userRepository;
    private RandomAlphaNumericString randomAlphaNumeric;

    public User getUser(String token) {
        User user = userRepository.findByToken(token);
        return user;
    }

    public String generateNewUserToken(User user){
        String token = randomAlphaNumeric.randomAlphaNumeric(30);
        user.setToken(token);
        return token;
    }
}
