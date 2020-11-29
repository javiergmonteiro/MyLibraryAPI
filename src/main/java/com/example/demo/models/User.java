package com.example.demo.models;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import net.bytebuddy.implementation.bind.annotation.Default;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import com.example.demo.security.SHA256;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @NotNull
    @NotBlank
    @Column(length = 30, unique = true)
    private String username;
    @NotNull
    @NotBlank
    @Column
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @Column
    private boolean active;
    @Column
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String passwordSalt;
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword(){return this.password;}

    public void setPassword(String password){
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean checkPassword(String rawPassword) throws NoSuchAlgorithmException {
        String hashedPassword = SHA256.makeHash(rawPassword, this.passwordSalt.getBytes());
        if (this.password.equals(hashedPassword)){
            return true;
        }
        else{
            return false;
        }
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }
}

