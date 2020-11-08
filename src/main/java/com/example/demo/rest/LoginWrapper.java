package com.example.demo.rest;

import com.example.demo.repositories.UserRepository;
import com.example.demo.models.User;
import com.example.demo.security.SHA256;
import javassist.NotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public class LoginWrapper {

    private String username;
    private String password;
    private UserRepository userRepository;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) throws NoSuchAlgorithmException {
        this.password = SHA256.makeHash(password);
    }

    public Optional<User> login() throws NotFoundException {
        Optional<User> user = userRepository.findByUsername(this.username);
        user.orElseThrow(() -> new UsernameNotFoundException("Not found "+this.username));
        if (user.get().checkPassword(this.password)){
            return user;
        }
        else{
            throw new NotFoundException("password is not correct");
        }
    }
}
