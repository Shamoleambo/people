package com.tidz.people.service;

import com.tidz.people.ResourceNotFoundException;
import com.tidz.people.model.Person;
import com.tidz.people.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.junit.jupiter.api.Assertions;

import org.mockito.Mockito;

import org.mockito.MockitoAnnotations;

import java.io.IOError;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

        Mockito.when(personRepository.save(Mockito.any(Person.class))).thenReturn(person);

        Person savedPerson = personService.save(person);

        Assertions.assertNotNull(savedPerson);
        Assertions.assertEquals("John Doe", savedPerson.getName());
        Assertions.assertEquals(30, savedPerson.getAge());
        Assertions.assertEquals("Engineer", savedPerson.getProfession());
        Mockito.verify(personRepository, Mockito.times(1)).save(person);
    }

    @Test
    void getAllPeopleShouldReturnAListOfPeople() {
        Person person1 = new Person();
        person1.setName("John Doe");
        person1.setAge(30);
        person1.setProfession("Engineer");

        Person person2 = new Person();
        person2.setName("Jane Doe");
        person2.setAge(28);
        person2.setProfession("Doctor");

        List<Person> people = Arrays.asList(person1, person2);
        Mockito.when(personRepository.findAll()).thenReturn(people);

        List<Person> result = personService.getAllPersons();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("John Doe", result.get(0).getName());
        Assertions.assertEquals("Jane Doe", result.get(1).getName());
        Mockito.verify(personRepository, Mockito.times(1)).findAll();
    }

    @Test
    void getPersonByIdShouldReturnAPerson() {
        Person person = new Person();
        person.setId(1L);
        person.setName("John Doe");
        person.setAge(30);
        person.setProfession("Engineer");

        Long id = 1L;
        Mockito.when(personRepository.findById(id)).thenReturn(Optional.of(person));

        Person personFound = personService.getPersonById(id);

        Assertions.assertNotNull(personFound);
        Assertions.assertEquals("John Doe", personFound.getName());
        Assertions.assertEquals(30, personFound.getAge());
        Assertions.assertEquals("Engineer", personFound.getProfession());
        Mockito.verify(personRepository, Mockito.times(1)).findById(id);
    }

    @Test
    void updateShouldUpdateThePersonWithTheProvidedId() {
        Person person = new Person();
        person.setName("John Doe");
        person.setAge(30);
        person.setProfession("Engineer");

        Person updatedPerson = new Person();
        updatedPerson.setName("John Smith");
        updatedPerson.setAge(35);
        updatedPerson.setProfession("Architect");

        Mockito.when(personRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(person));
        Mockito.when(personRepository.save(Mockito.any(Person.class))).thenReturn(updatedPerson);

        Long id = 1L;
        Person result = personService.update(id, updatedPerson);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("John Smith", result.getName());
        Assertions.assertEquals(35, result.getAge());
        Assertions.assertEquals("Architect", result.getProfession());

        Mockito.verify(personRepository, Mockito.times(1)).findById(id);
        Mockito.verify(personRepository, Mockito.times(1)).save(person);

    }

    @Test
    void deleteShouldDeleteThePersonWithTheProvidedId() {
        Long id = 1L;
        Person person = new Person(id, "John Doe", 30, "Engineer");

        Mockito.when(personRepository.findById(id)).thenReturn(Optional.of(person));
        Mockito.doNothing().when(personRepository).delete(person);

        personService.delete(id);

        Mockito.verify(personRepository, Mockito.times(1)).findById(id);
        Mockito.verify(personRepository, Mockito.times(1)).delete(person);
    }

    @Test
    void shouldThrowAnErrorIfPeronIsNotFound() {
        Mockito.when(personRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> personService.getPersonById(1L));
    }

    @Test
    void shouldThrowAnErrorIfPersonToBeUpdatedIsNotFound() {
        Person updatedPerson = new Person(1L, "John Doe", 35, "Engineer");

        Mockito.when(personRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            personService.update(1L, updatedPerson);
        });
    }
}
