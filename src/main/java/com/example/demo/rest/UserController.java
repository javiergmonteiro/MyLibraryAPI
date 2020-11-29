package com.example.demo.rest;

import com.example.demo.models.Login;
import com.example.demo.models.Register;
import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import com.example.demo.security.TokenAuthentication;
import com.example.demo.services.UserService;
import com.example.demo.utils.RandomAlphaNumericString;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenAuthentication tokenAuthentication;
    @Autowired
    private UserService userService;
    private RandomAlphaNumericString randomAlphaNumericString;

    @PostMapping("/login")
    public ResponseEntity<User> doLogin(@Valid @RequestBody Login login){
        try{
            User user = userService.login(login);
            return ResponseEntity.ok(user);
        }
        catch (NotFoundException | NoSuchAlgorithmException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<User> doRegistration(@RequestBody User user) throws URISyntaxException{
        userService.register(user);
        return ResponseEntity.ok(user);
    }
}
