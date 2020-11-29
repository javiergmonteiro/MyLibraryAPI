package com.example.demo.models;

import com.example.demo.security.SHA256;
import com.sun.istack.NotNull;

import java.security.NoSuchAlgorithmException;

public class Login {

    @NotNull
    private String username;
    @NotNull
    private String password;

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password){
        this.password = password;
    }
}
