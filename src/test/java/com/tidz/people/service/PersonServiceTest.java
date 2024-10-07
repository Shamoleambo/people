package com.tidz.people.service;

import com.tidz.people.model.Person;
import com.tidz.people.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.MockitoAnnotations;

public class PersonServiceTest {

    @InjectMocks
    private PersonService personService;

    @Mock
    private PersonRepository personRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void savePersonShouldReturnSavedPerson() {
        Person person = new Person();
        person.setName("John Doe");
        person.setAge(30);
        person.setProfession("Engineer");

        when(personRepository.save(any(Person.class))).thenReturn(person);

        Person savedPerson = personService.save(person);

        assertNotNull(savedPerson);
        assertEquals("John Doe", savedPerson.getName());
        assertEquals(30, savedPerson.getAge());
        assertEquals("Engineer", savedPerson.getProfession());
        verify(personRepository, times(1)).save(person);
    }
}
