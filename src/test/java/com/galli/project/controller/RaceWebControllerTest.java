package com.galli.project.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = RaceWebController.class)
class RaceWebControllerTest {

	@Autowired
	private MockMvc mvc;

	@Test
	@DisplayName("Test races page has status 2xx")
	void test1() throws Exception {
		mvc.perform(get("/races")).andExpect(status().is2xxSuccessful());
	}

}
