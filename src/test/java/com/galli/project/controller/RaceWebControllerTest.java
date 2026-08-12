package com.galli.project.controller;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;

import com.galli.project.model.Circuit;
import com.galli.project.model.Pilot;
import com.galli.project.model.Race;
import com.galli.project.service.RaceService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = RaceWebController.class)
class RaceWebControllerTest {

	@Autowired
	private MockMvc mvc;
	@MockitoBean
	private RaceService raceService;

	@Test
	@DisplayName("Test races page has status 2xx")
	void test1() throws Exception {
		mvc.perform(get("/races")).andExpect(status().is2xxSuccessful());
	}

	@Test
	@DisplayName("Test return races page")
	void test2() throws Exception {
		ModelAndViewAssert.assertViewName(
				mvc.perform(get("/races")).andReturn().getModelAndView(),
				"races-list");
	}

	@Test
	@DisplayName("Test view shows races")
	void test3() throws Exception {
		Circuit circuit = new Circuit(1L, "circuit", 1000L);
		Pilot pilot = new Pilot(1L, "pilot");
		Set<Pilot> pilots = new HashSet<>();
		pilots.add(pilot);
		List<Race> races = asList(new Race(1L, "test", circuit, pilots));

		when(raceService.getAllRaces()).thenReturn(races);

		mvc.perform(get("/races"))
				.andExpect(view().name("races-list"))
				.andExpect(model().attribute("races", races))
				.andExpect(model().attribute("message", ""));
	}

	@Test
	@DisplayName("Test view shows message when there are no races")
	void test4() throws Exception {
		when(raceService.getAllRaces()).thenReturn(emptyList());
		mvc.perform(get("/races")).andExpect(view().name("races-list"))
				.andExpect(model().attribute("races", emptyList()))
				.andExpect(model().attribute("message", "No Races"));
	}

	@Test
	@DisplayName("View race when it is found")
	void test5() throws Exception {
		Circuit circuit = new Circuit(1L, "circuit", 1000L);
		Pilot pilot = new Pilot(1L, "pilot");
		Set<Pilot> pilots = new HashSet<>();
		pilots.add(pilot);
		Race race = new Race(1L, "test", circuit, pilots);
		when(raceService.getRaceById(1L)).thenReturn(race);
		mvc.perform(get("/races/view/1")).andExpect(view().name("view-race"))
				.andExpect(model().attribute("race", race))
				.andExpect(model().attribute("message", ""));
	}

	@Test
	@DisplayName("View race when it is not found")
	void test6() throws Exception {
		when(raceService.getRaceById(1L)).thenReturn(null);
		mvc.perform(get("/races/view/1")).andExpect(view().name("view-race"))
				.andExpect(model().attribute("race", nullValue()))
				.andExpect(model().attribute("message",
						"No Race found with id 1"));
	}

}
