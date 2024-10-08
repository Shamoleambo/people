package com.tidz.people.service;

import com.tidz.people.ResourceNotFoundException;
import com.tidz.people.model.Person;
import com.tidz.people.repository.PersonRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {

    private final PersonRepository repository;

    @Autowired
    public PersonService(PersonRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Person save(Person person) {
        return repository.save(person);
    }

    public List<Person> getAllPersons() {
        return repository.findAll();
    }

    public Person getPersonById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Person with id " + id + " not found"));
    }

    @Transactional
    public Person update(Long id, Person updatedPerson) {
        return repository.findById(id).map(person -> {
            person.setName(updatedPerson.getName());
            person.setAge(updatedPerson.getAge());
            person.setProfession(updatedPerson.getProfession());
            return repository.save(person);
        }).orElseThrow(() -> new ResourceNotFoundException("Person with id " + id + " not found"));
    }
}
