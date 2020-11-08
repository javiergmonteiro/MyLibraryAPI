package com.example.demo.rest;

import com.example.demo.repositories.PersonDAO;
import com.example.demo.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/person")
public class PersonRest {

    @Autowired
    private PersonDAO personDAO;

    @PostMapping("")
    public void save(@RequestBody Person person){
        personDAO.save(person);
    }

    @GetMapping("")
    public List<Person> list(){
        return personDAO.findAll();
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Integer id){
        personDAO.deleteById(id);
    }
}
