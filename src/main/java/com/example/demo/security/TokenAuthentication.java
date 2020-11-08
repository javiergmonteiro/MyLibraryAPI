package com.example.demo.security;

import com.example.demo.repositories.UserRepository;
import com.example.demo.models.User;

public class TokenAuthentication {

    private UserRepository userRepository;

    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private static String randomAlphaNumeric(int count){
        StringBuilder builder = new StringBuilder();
        while (count-- != 0){
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

    public User getUser(String token) {
        User user = userRepository.findByToken(token);
        return user;
    }

    public String generateNewUserToken(User user){
        String token = randomAlphaNumeric(15);
        user.setToken(token);
        return token;
    }
}
