package com.galli.project.controller;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import com.galli.project.model.PilotDTO;
import com.galli.project.service.PilotService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = PilotWebController.class)
class PilotWebControllerTest {

	@Autowired
	private MockMvc mvc;
	@MockitoBean
	private PilotService pilotService;
	@Autowired
	private PilotWebController controller;

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

	@Test
	@DisplayName("Edit pilot when it is found")
	void test5() throws Exception {
		Pilot pilot = new Pilot(1L, "test");
		when(pilotService.getPilotById(1L)).thenReturn(pilot);
		mvc.perform(get("/pilots/edit/1")).andExpect(view().name("edit-pilot"))
				.andExpect(model().attribute("pilot", pilot))
				.andExpect(model().attribute("message", ""));
	}

	@Test
	@DisplayName("Edit pilot when it is not found")
	void test6() throws Exception {
		when(pilotService.getPilotById(1L)).thenReturn(null);
		mvc.perform(get("/pilots/edit/1")).andExpect(view().name("edit-pilot"))
				.andExpect(model().attribute("pilot", nullValue()))
				.andExpect(model().attribute("message",
						"No Pilot found with id 1"));
	}

	@Test
	@DisplayName("Edit new pilot")
	void test7() throws Exception {
		mvc.perform(get("/pilots/new")).andExpect(view().name("edit-pilot"))
				.andExpect(model().attribute("pilot", new Pilot()))
				.andExpect(model().attribute("message", ""));
		verifyNoInteractions(pilotService);
	}

	@Test
	@DisplayName("Post pilot without id should insert new pilot")
	void test8() throws Exception {
		mvc.perform(post("/pilots/save").param("name", "test name"))
				.andExpect(view().name("redirect:/pilots"));
		verify(pilotService)
				.insertNewPilot(new Pilot(null, "test name"));
	}

	@Test
	@DisplayName("Post pilot with id should update existing pilot")
	void test9() throws Exception {
		mvc.perform(post("/pilots/save").param("id", "1").param("name",
				"test name"))
				.andExpect(view().name("redirect:/pilots"));
		verify(pilotService).updatePilotById(1L,
				new Pilot(1L, "test name"));
	}

	@Test
	@DisplayName("Test that a DTO is used when saving a pilot")
	void test10() {
		PilotDTO pilotDTO = spy(new PilotDTO(1L, "test name"));
		controller.savePilot(pilotDTO);
		verify(pilotDTO).getId();
		verify(pilotDTO).getName();
	}

	@Test
	@DisplayName("Test delete")
	void test11() throws Exception {
		mvc.perform(post("/pilots/delete/1"))
				.andExpect(view().name("redirect:/pilots"));
		verify(pilotService).deletePilotById(1L);
	}
}
