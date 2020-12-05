package com.example.demo.rest;

import com.example.demo.models.Login;
import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import com.example.demo.security.TokenAuthentication;
import com.example.demo.services.UserService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenAuthentication tokenAuthentication;
    @Autowired
    private UserService userService;

    @GetMapping("")
    public ResponseEntity<List> getAllUsers(){
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @PostMapping("")
    public ResponseEntity<User> createUser(@RequestBody User user) throws URISyntaxException{
        userService.register(user);
        return ResponseEntity.ok(user);
    }

    @GetMapping("{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id){
        Optional<User> _user = userRepository.findById(id);
        if (_user.isPresent()){
            return ResponseEntity.ok(_user.get());
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("{id}")
    public ResponseEntity<ResponseEntity> deleteUserById(@PathVariable Integer id){
        Optional<User> _user = userRepository.findById(id);
        if (_user.isPresent()){
            userRepository.delete(_user.get());
            return ResponseEntity.accepted().build();
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

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
