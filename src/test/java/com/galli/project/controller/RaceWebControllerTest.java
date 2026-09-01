package com.galli.project.controller;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import com.galli.project.model.RaceDTO;
import com.galli.project.service.RaceService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = RaceWebController.class)
class RaceWebControllerTest {

	@Autowired
	private MockMvc mvc;
	@MockitoBean
	private RaceService raceService;
	@Autowired
	private RaceWebController controller;

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
		Set<Pilot> pilots = new HashSet<>(asList(pilot));

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

	@Test
	@DisplayName("Edit new race")
	void test7() throws Exception {
		mvc.perform(get("/races/new")).andExpect(view().name("view-race"))
				.andExpect(model().attribute("race", new Race()))
				.andExpect(model().attribute("message", ""));
		verifyNoInteractions(raceService);
	}

	@Test
	@DisplayName("Post race without id should insert new race")
	void test8() throws Exception {
		when(raceService.insertNewRace(any()))
				.thenReturn(new Race(1L, null, null, null));

		Circuit circuit = new Circuit(1L, "circuit", 1000L);
		Pilot pilot = new Pilot(1L, "pilot");
		Set<Pilot> pilots = new HashSet<>(asList(pilot));

		mvc.perform(post("/races/save")
				.param("name", "test name")
				.param("circuit.id", "1")
				.param("circuit.name", "circuit")
				.param("circuit.length", "1000")
				.param("pilotsList[0].id", "1")
				.param("pilotsList[0].name", "pilot"))
				.andExpect(view().name("redirect:/races/view/1"));
		verify(raceService)
				.insertNewRace(new Race(null, "test name", circuit, pilots));
	}

	@Test
	@DisplayName("Post race with id should update existing race")
	void test9() throws Exception {
		when(raceService.updateRaceById(anyLong(), any()))
				.thenReturn(new Race(1L, null, null, null));

		Circuit circuit = new Circuit(1L, "circuit", 1000L);
		Set<Pilot> pilots = new HashSet<>(asList(new Pilot(1L, "pilot")));

		mvc.perform(
				post("/races/save").param("id", "1").param("name", "test name")
						.param("circuit.id", "1")
						.param("circuit.name", "circuit")
						.param("circuit.length", "1000")
						.param("pilotsList[0].id", "1")
						.param("pilotsList[0].name", "pilot"))
				.andExpect(view().name("redirect:/races/view/1"));
		verify(raceService).updateRaceById(1L,
				new Race(1L, "test name", circuit, pilots));
	}

	@Test
	@DisplayName("Test that a DTO is used when saving a race")
	void test10() {
		when(raceService.updateRaceById(anyLong(), any()))
				.thenReturn(new Race(1L, null, null, null));

		Circuit circuit = new Circuit(1L, "circuit", 1000L);
		List<Pilot> pilots = asList(new Pilot(1L, "pilot"));

		RaceDTO raceDTO = spy(new RaceDTO(1L, "test name", circuit, pilots));
		controller.saveRace(raceDTO);
		verify(raceDTO).getId();
		verify(raceDTO).getName();
		verify(raceDTO).getCircuit();
		verify(raceDTO).getPilotsList();
	}

	@Test
	@DisplayName("Test delete")
	void test11() throws Exception {
		mvc.perform(post("/races/delete/1"))
				.andExpect(view().name("redirect:/races"));
		verify(raceService).deleteRaceById(1L);
	}
}
