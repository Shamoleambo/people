package com.tidz.people.controller;

import com.tidz.people.model.Person;
import com.tidz.people.service.PersonService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
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
}
