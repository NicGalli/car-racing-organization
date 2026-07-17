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

import com.galli.project.model.Circuit;
import com.galli.project.model.CircuitDTO;
import com.galli.project.service.CircuitService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CircuitWebController.class)
class CircuitWebControllerTest {

	@Autowired
	private MockMvc mvc;
	@MockitoBean
	private CircuitService circuitService;
	@Autowired
	private CircuitWebController controller;

	@Test
	@DisplayName("Test circuits page has status 2xx")
	void test1() throws Exception {
		mvc.perform(get("/circuits")).andExpect(status().is2xxSuccessful());
	}

	@Test
	@DisplayName("Test return circuits page")
	void test2() throws Exception {
		ModelAndViewAssert.assertViewName(
				mvc.perform(get("/circuits")).andReturn().getModelAndView(),
				"circuits-list");
	}

	@Test
	@DisplayName("Test view shows circuits")
	void test3() throws Exception {
		List<Circuit> circuits = asList(new Circuit(1L, "test", 1000L));

		when(circuitService.getAllCircuits()).thenReturn(circuits);

		mvc.perform(get("/circuits"))
				.andExpect(view().name("circuits-list"))
				.andExpect(model().attribute("circuits", circuits))
				.andExpect(model().attribute("message", ""));
	}

	@Test
	@DisplayName("Test view shows message when there are no circuits")
	void test4() throws Exception {
		when(circuitService.getAllCircuits()).thenReturn(emptyList());
		mvc.perform(get("/circuits")).andExpect(view().name("circuits-list"))
				.andExpect(model().attribute("circuits", emptyList()))
				.andExpect(model().attribute("message", "No Circuits"));
	}

	@Test
	@DisplayName("Edit circuit when it is found")
	void test5() throws Exception {
		Circuit circuit = new Circuit(1L, "test", 1000L);
		when(circuitService.getCircuitById(1L)).thenReturn(circuit);
		mvc.perform(get("/circuits/edit/1"))
				.andExpect(view().name("edit-circuit"))
				.andExpect(model().attribute("circuit", circuit))
				.andExpect(model().attribute("message", ""));
	}

	@Test
	@DisplayName("Edit circuit when it is not found")
	void test6() throws Exception {
		when(circuitService.getCircuitById(1L)).thenReturn(null);
		mvc.perform(get("/circuits/edit/1"))
				.andExpect(view().name("edit-circuit"))
				.andExpect(model().attribute("circuit", nullValue()))
				.andExpect(model().attribute("message",
						"No Circuit found with id 1"));
	}

	@Test
	@DisplayName("Edit new circuit")
	void test7() throws Exception {
		mvc.perform(get("/circuits/new")).andExpect(view().name("edit-circuit"))
				.andExpect(model().attribute("circuit", new Circuit()))
				.andExpect(model().attribute("message", ""));
		verifyNoInteractions(circuitService);
	}

	@Test
	@DisplayName("Post circuit without id should insert new circuit")
	void test8() throws Exception {
		mvc.perform(post("/circuits/save").param("name", "test name")
				.param("length", "1000"))
				.andExpect(view().name("redirect:/circuits"));
		verify(circuitService)
				.insertNewCircuit(new Circuit(null, "test name", 1000L));
	}

	@Test
	@DisplayName("Post circuit with id should update existing circuit")
	void test9() throws Exception {
		mvc.perform(post("/circuits/save").param("id", "1").param("name",
				"test name").param("length",
						"1000"))
				.andExpect(view().name("redirect:/circuits"));
		verify(circuitService).updateCircuitById(1L,
				new Circuit(1L, "test name", 1000L));
	}

	@Test
	@DisplayName("Test that a DTO is used when saving a circuit")
	void test10() {
		CircuitDTO circuitDTO = spy(new CircuitDTO(1L, "test name", 1000L));
		controller.saveCircuit(circuitDTO);
		verify(circuitDTO).getId();
		verify(circuitDTO).getName();
		verify(circuitDTO).getLength();
	}

	@Test
	@DisplayName("Test delete")
	void test11() throws Exception {
		mvc.perform(post("/circuits/delete/1"))
				.andExpect(view().name("redirect:/circuits"));
		verify(circuitService).deleteCircuitById(1L);
	}
}
