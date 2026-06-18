package com.galli.project.controller;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;

import com.galli.project.model.Pilot;
import com.galli.project.service.PilotService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = PilotWebController.class)
class PilotWebControllerTest {

	@Autowired
	private MockMvc mvc;
	@MockitoBean
	private PilotService pilotService;

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

	@Test
	@DisplayName("Test view shows pilots")
	void test3() throws Exception {
		List<Pilot> pilots = asList(new Pilot(1L, "test"));

		when(pilotService.getAllPilots()).thenReturn(pilots);

		mvc.perform(get("/pilots"))
				.andExpect(view().name("pilots-list"))
				.andExpect(model().attribute("pilots", pilots))
				.andExpect(model().attribute("message", ""));
	}

	@Test
	@DisplayName("Test view shows message when there are no pilots")
	void test4() throws Exception {
		when(pilotService.getAllPilots()).thenReturn(emptyList());
		mvc.perform(get("/pilots")).andExpect(view().name("pilots-list"))
				.andExpect(model().attribute("pilots", emptyList()))
				.andExpect(model().attribute("message", "No Pilots"));
	}
}
