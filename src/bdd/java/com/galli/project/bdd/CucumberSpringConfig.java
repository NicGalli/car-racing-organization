package com.galli.project.bdd;

import org.springframework.boot.jpa.test.autoconfigure.AutoConfigureTestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import io.cucumber.spring.CucumberContextConfiguration;

@SpringBootTest
@CucumberContextConfiguration
@AutoConfigureTestEntityManager
@AutoConfigureMockMvc
@Transactional
@DirtiesContext
public class CucumberSpringConfig {}
