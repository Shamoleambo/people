package com.tidz.people.controller;

import com.tidz.people.exceptions.ResourceNotFoundException;
import com.tidz.people.model.Person;
import com.tidz.people.service.PersonService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

public class PersonControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private PersonController controller;

    @Mock
    private PersonService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void saveShouldReturnSavedPerson() throws Exception {
        Person person = new Person(1L, "John Doe", 30, "Engineer");

        Mockito.when(service.save(Mockito.any(Person.class))).thenReturn(person);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"John Doe\", \"age\": 30, \"profession\": \"Engineer\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.name", Matchers.is("John Doe")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.age", Matchers.is(30)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.profession", Matchers.equalToIgnoringCase("engineer")));

        Mockito.verify(service, Mockito.times(1)).save(Mockito.any(Person.class));
    }

    @Test
    void getAllShouldReturnAllPersons() throws Exception {
        Person person1 = new Person(1L, "John Doe", 30, "Engineer");
        Person person2 = new Person(2L, "Mary Sue", 22, "Programmer");
        Person person3 = new Person(3L, "Gary Thumb", 26, "Assistant");
        List<Person> people = Arrays.asList(person1, person2, person3);

        Mockito.when(service.getAllPersons()).thenReturn(people);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/people").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.body", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body", Matchers.hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].name", Matchers.is("John Doe")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].age", Matchers.is(30)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[0].profession", Matchers.equalToIgnoringCase("Engineer")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].name", Matchers.is("Mary Sue")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].age", Matchers.is(22)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[1].profession", Matchers.equalToIgnoringCase("programmer")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].name", Matchers.is("Gary Thumb")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].age", Matchers.is(26)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body[2].profession", Matchers.equalToIgnoringCase("assistant")));


        Mockito.verify(service, Mockito.times(1)).getAllPersons();
    }

    @Test
    void getPersonByIdShouldReturnNotFoundError() throws Exception {
        Long id = 1L;

        Mockito.when(service.getPersonById(id)).thenThrow(new ResourceNotFoundException("Person with id " + id + " not found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/people/{id}", id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo("Person with id " + id + " not found")));
    }

    @Test
    void getPersonByIdShouldReturnAPersonWithThatId() throws Exception {
        Long id = 1L;
        Person person = new Person(id, "John Doe", 30, "Engineer");

        Mockito.when(service.getPersonById(id)).thenReturn(person);


        mockMvc.perform(MockMvcRequestBuilders.get("/api/people/{id}", id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.name", Matchers.is("John Doe")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.age", Matchers.is(30)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.profession", Matchers.is("Engineer")));

        Mockito.verify(service, Mockito.times(1)).getPersonById(id);
    }

    @Test
    void updatePersonShouldReturnAnErrorIfPersonIsNotFound() throws Exception {
        Long id = 1L;
        Mockito.when(service.update(Mockito.eq(id), Mockito.any(Person.class))).thenThrow(new ResourceNotFoundException("Person updated"));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/people/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Mary Sue\", \"age\": 33, \"profession\": \"Architect\"}"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo("Person updated")));

        Mockito.verify(service, Mockito.times(1)).update(Mockito.eq(id), Mockito.any(Person.class));
    }

    @Test
    void updatePersonShouldReturn200() throws Exception {
        Person updatedPerson = new Person(1L, "John Doe", 30, "Engineer");
        Mockito.when(service.update(Mockito.anyLong(), Mockito.any(Person.class))).thenReturn(updatedPerson);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/people/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"John Doe\", \"age\": 30, \"profession\": \"Engineer\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo("Success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.name", Matchers.equalTo(updatedPerson.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.age", Matchers.is(30)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.profession", Matchers.equalTo("Engineer")));

        Mockito.verify(service, Mockito.times(1)).update(Mockito.anyLong(), Mockito.any(Person.class));
    }

    @Test
    void deletePersonShouldReturn404IfPersonIsNotFound() throws Exception {
        Mockito.doThrow(new ResourceNotFoundException("Delete person error")).when(service).delete(Mockito.anyLong());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/people/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo("Delete person error")));

        Mockito.verify(service, Mockito.times(1)).delete(Mockito.anyLong());
    }

    @Test
    void deletePersonShouldReturn200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/people/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo("Person deleted")));

        Mockito.verify(service, Mockito.times(1)).delete(Mockito.anyLong());
    }
}
