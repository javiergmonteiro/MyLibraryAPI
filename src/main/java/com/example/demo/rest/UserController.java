package com.example.demo.rest;

import com.example.demo.models.User;
import javassist.NotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("user")
public class UserController {

    @PostMapping("/login")
    public String doLogin(@RequestBody LoginWrapper loginWrapper) throws NotFoundException {
        Optional<User> user = loginWrapper.login();
        user.orElseThrow( () -> new UsernameNotFoundException("Authentication failed"));
        return user.get().getToken();
    }
}
