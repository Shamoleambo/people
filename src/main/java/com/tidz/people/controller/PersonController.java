package com.tidz.people.controller;

import com.tidz.people.exceptions.ResourceNotFoundException;
import com.tidz.people.model.Person;
import com.tidz.people.response.ApiResponse;
import com.tidz.people.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getPersonById(@PathVariable("id") Long id) {
        try {
            Person person = service.getPersonById(id);
            return ResponseEntity.ok(new ApiResponse("Success", person));

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updatePerson(@PathVariable("id") Long id, @RequestBody Person person) {
        try {
            Person updatedPerson = service.update(id, person);
            return ResponseEntity.ok(new ApiResponse("Success", updatedPerson));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deletePerson(@PathVariable("id") Long id) {
        try {
            service.delete(id);
            return null;
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
