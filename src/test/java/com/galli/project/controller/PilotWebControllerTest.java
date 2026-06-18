package com.galli.project.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = PilotWebController.class)
class PilotWebControllerTest {

	@Autowired
	private MockMvc mvc;

	@Test
	@DisplayName("Test pilots page has status 2xx")
	void test1() throws Exception {
		mvc.perform(get("/pilots")).andExpect(status().is2xxSuccessful());
	}

	@Test
	@DisplayName("Test return pilots page")
	void test2() throws Exception {
		ModelAndViewAssert.assertViewName(
				mvc.perform(get("/pilots")).andReturn().getModelAndView(),
				"pilots-list");
	}
}
