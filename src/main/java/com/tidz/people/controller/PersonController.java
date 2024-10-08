package com.tidz.people.controller;

import com.tidz.people.model.Person;
import com.tidz.people.response.ApiResponse;
import com.tidz.people.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/people")
public class PersonController {

    private final PersonService service;

    @Autowired
    public PersonController(PersonService service) {
        this.service = service;
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse> savePerson(@RequestBody Person person) {
        Person savedPerson = service.save(person);
        return ResponseEntity.ok(new ApiResponse("Success", savedPerson));
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse> getAllPeople() {
        List<Person> people = service.getAllPersons();
        return ResponseEntity.ok(new ApiResponse("Success", people));
    }

}
