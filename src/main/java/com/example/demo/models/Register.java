package com.example.demo.models;

import com.sun.istack.NotNull;

import javax.validation.constraints.NotBlank;

public class Register {

    @NotBlank
    private String username;
    @NotBlank
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
