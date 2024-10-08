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
}
