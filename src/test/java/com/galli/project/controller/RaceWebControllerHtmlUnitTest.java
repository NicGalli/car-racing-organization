package com.galli.project.controller;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.htmlunit.ElementNotFoundException;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlButton;
import org.htmlunit.html.HtmlForm;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.html.HtmlTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.galli.project.model.Circuit;
import com.galli.project.model.Pilot;
import com.galli.project.model.Race;
import com.galli.project.service.RaceService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = RaceWebController.class)
class RaceWebControllerHtmlUnitTest {

	@Autowired
	private WebClient webClient;

	@MockitoBean
	private RaceService service;

	@Test
	@DisplayName("Test Races page title")
	void test1() throws Exception {
		HtmlPage page = webClient.getPage("/races");
		assertThat(page.getTitleText()).isEqualTo("Races");
	}

	@Test
	@DisplayName("Test Races page when there are no races")
	void test2() throws Exception {
		when(service.getAllRaces()).thenReturn(emptyList());
		HtmlPage page = webClient.getPage("/races");
		assertThat(page.getBody().getTextContent()).contains("No Races");
	}

	@Test
	@DisplayName("Test Races page with races should show them in a table")
	void test3() throws Exception {
		Circuit circuit = new Circuit(1L, "circuit", 1000L);
		Set<Pilot> pilots = new HashSet<>(asList(new Pilot(1L, "pilot")));

		when(service.getAllRaces())
				.thenReturn(asList(new Race(1L, "first race", circuit, pilots),
						new Race(2L, "second race", circuit, pilots)));
		HtmlPage page = webClient.getPage("/races");
		assertThat(page.getBody().getTextContent())
				.doesNotContain("No Races");
		HtmlTable table = page.getHtmlElementById("races_table");
		String textBlock = """
				Races
				ID	Name
				1	first race	View
				2	second race	View""";
		assertThat(table.asNormalizedText()).isEqualTo(textBlock);
		page.getAnchorByHref("/races/view/1");
		page.getAnchorByHref("/races/view/2");
	}

	@Test
	@DisplayName("Test races page should provide a link for creating a new race")
	void test4() throws Exception {
		HtmlPage page = webClient.getPage("/races");
		assertThat(page.getAnchorByText("New Race").getHrefAttribute())
				.isEqualTo("/races/new");
	}

	@Test
	@DisplayName("Test view non existing race")
	void test5() throws Exception {
		when(service.getRaceById(1L)).thenReturn(null);
		HtmlPage page = webClient.getPage("/races/view/1");
		assertThat(page.getTitleText()).isEqualTo("Race View");
		assertThat(page.getBody().getTextContent())
				.contains("No Race found with id 1");
	}

	@Test
	@DisplayName("Test view existing race with pilots")
	void test6() throws Exception {
		Circuit circuit = new Circuit(1L, "circuit", 1000L);
		Set<Pilot> pilots = new HashSet<>(asList(new Pilot(1L, "first pilot"),
				new Pilot(2L, "second pilot")));

		when(service.getRaceById(1L))
				.thenReturn(new Race(1L, "original name", circuit, pilots));

		HtmlPage page = webClient.getPage("races/view/1");

		assertThat(page.getBody().getTextContent())
				.doesNotContain("No Race found with id 1");

		HtmlTable table = page.getHtmlElementById("pilots_table");
		String textBlock = """
				Pilots
				ID	Name
				1	first pilot	Remove
				2	second pilot	Remove""";

		assertThat(table.asNormalizedText()).isEqualTo(textBlock);
		assertThat(page.getTitleText()).isEqualTo("Race View");
	}

	@Test
	@DisplayName("Test view existing race with no pilots")
	void test7() throws Exception {
		Circuit circuit = new Circuit(1L, "circuit", 1000L);

		when(service.getRaceById(1L))
				.thenReturn(new Race(1L, "original name", circuit, emptySet()));

		HtmlPage page = webClient.getPage("races/view/1");

		assertThat(page.getBody().getTextContent())
				.doesNotContain("No Race found with id 1");
		assertThat(page.getBody().getTextContent())
				.contains("No Pilots");
	}

	@Test
	@DisplayName("Test edit name in existing race")
	void test8() throws Exception {
		Circuit circuit1 = new Circuit(1L, "first circuit", 1000L);
		Circuit circuit2 = new Circuit(2L, "second circuit", 2000L);

		Set<Pilot> pilots = new HashSet<>(asList(new Pilot(1L, "first pilot"),
				new Pilot(2L, "second pilot")));

		when(service.getRaceById(1L))
				.thenReturn(new Race(1L, "original name", circuit1, pilots));
		when(service.updateRaceById(any(), any()))
				.thenReturn(new Race(1L, "modified name", circuit2, pilots));
		when(service.getAllCircuits())
				.thenReturn(asList(circuit1, circuit2));

		HtmlPage page = webClient.getPage("races/view/1");

		final HtmlForm form = page.getFormByName("race_form");
		form.getInputByValue("original name")
				.setValue("modified name");
		assertEquals(
				form.getSelectByName("circuit.id").getSelectedOptions()
						.getFirst().getText(),
				"first circuit");
		form.getSelectByName("circuit.id").getOptionByText("second circuit")
				.setSelected(true);
		form.getButtonByName("btn_edit_name").click();

		ArgumentCaptor<Race> raceCaptor = ArgumentCaptor.forClass(Race.class);
		verify(service).updateRaceById(eq(1L),
				raceCaptor.capture());

		Race race = raceCaptor.getValue();
		assertEquals(2L, race.getCircuit().getId());
		assertEquals("modified name", race.getName());
		assertThat(race.getPilotsList())
				.extracting(Pilot::getId)
				.containsExactly(1L, 2L);
	}

	@Test
	@DisplayName("Test edit circuit in existing race")
	void test9() throws Exception {
		Circuit circuit1 = new Circuit(1L, "first circuit", 1000L);
		Circuit circuit2 = new Circuit(2L, "second circuit", 2000L);

		Set<Pilot> pilots = new HashSet<>(asList(new Pilot(1L, "first pilot"),
				new Pilot(2L, "second pilot")));

		when(service.getRaceById(1L))
				.thenReturn(new Race(1L, "original name", circuit1, pilots));
		when(service.updateRaceById(any(), any()))
				.thenReturn(new Race(1L, "modified name", circuit2, pilots));
		when(service.getAllCircuits())
				.thenReturn(asList(circuit1, circuit2));

		HtmlPage page = webClient.getPage("races/view/1");

		final HtmlForm form = page.getFormByName("race_form");
		form.getInputByValue("original name")
				.setValue("modified name");
		assertEquals(
				form.getSelectByName("circuit.id").getSelectedOptions()
						.getFirst().getText(),
				"first circuit");
		form.getSelectByName("circuit.id").getOptionByText("second circuit")
				.setSelected(true);
		form.getButtonByName("btn_edit_circuit").click();

		ArgumentCaptor<Race> raceCaptor = ArgumentCaptor.forClass(Race.class);
		verify(service).updateRaceById(eq(1L),
				raceCaptor.capture());

		Race race = raceCaptor.getValue();
		assertEquals(2L, race.getCircuit().getId());
		assertEquals("modified name", race.getName());
		assertThat(race.getPilotsList())
				.extracting(Pilot::getId)
				.containsExactly(1L, 2L);
	}

	@Test
	@DisplayName("Test remove pilot")
	void test10() throws Exception {
		Circuit circuit1 = new Circuit(1L, "first circuit", 1000L);
		Circuit circuit2 = new Circuit(2L, "second circuit", 2000L);

		Set<Pilot> pilots = new HashSet<>(asList(new Pilot(1L, "first pilot"),
				new Pilot(2L, "second pilot")));

		when(service.getRaceById(1L))
				.thenReturn(new Race(1L, "original name", circuit1, pilots));
		when(service.updateRaceById(any(), any()))
				.thenReturn(new Race(1L, "modified name", circuit2, pilots));
		when(service.getAllCircuits())
				.thenReturn(asList(circuit1, circuit2));

		HtmlPage page = webClient.getPage("races/view/1");

		HtmlButton removeButton = page.getFirstByXPath(
				"//tr[td[normalize-space()='first pilot']]//button[@name='btn_remove_pilot']");
		removeButton.click();

		verify(service).deletePilotFromRaceById(1L, 1L);
	}

	@Test
	@DisplayName("Test add pilot")
	void test11() throws Exception {
		Circuit circuit1 = new Circuit(1L, "first circuit", 1000L);

		Set<Pilot> pilots = new HashSet<>(asList(new Pilot(1L, "first pilot"),
				new Pilot(2L, "second pilot")));

		when(service.getRaceById(1L))
				.thenReturn(new Race(1L, "original name", circuit1, pilots));
		when(service.getAllOtherPilots(any()))
				.thenReturn(asList(new Pilot(3L, "third pilot"),
						new Pilot(4L, "fourth pilot")));
		
		HtmlPage page = webClient.getPage("races/view/1");

		final HtmlForm form = page.getFormByName("add_pilot_form");

		form.getSelectByName("pilotId").getOptionByText("third pilot")
				.setSelected(true);
		form.getButtonByName("btn_add_pilot").click();

		verify(service).addPilotToRaceById(1L, 3L);
	}

	@Test
	@DisplayName("Test delete race")
	void test12() throws Exception {
		Circuit circuit1 = new Circuit(1L, "first circuit", 1000L);

		Set<Pilot> pilots = new HashSet<>(asList(new Pilot(1L, "first pilot"),
				new Pilot(2L, "second pilot")));

		when(service.getRaceById(1L))
				.thenReturn(new Race(1L, "original name", circuit1, pilots));
		
		HtmlPage page = webClient.getPage("races/view/1");

		final HtmlForm form = page.getFormByName("delete_race_form");
		form.getButtonByName("btn_delete_race").click();

		verify(service).deleteRaceById(1L);
	}

	@Test
	@DisplayName("Test new race")
	void test13() throws Exception {
		Circuit circuit1 = new Circuit(1L, "first circuit", 1000L);
		Circuit circuit2 = new Circuit(2L, "second circuit", 2000L);

		when(service.insertNewRace(any()))
				.thenReturn(new Race(1L, "A name", circuit2, null));
		when(service.getAllCircuits())
				.thenReturn(asList(circuit1, circuit2));
		HtmlPage page = webClient.getPage("races/new");

		assertThrows(ElementNotFoundException.class,
				() -> page.getFormByName("delete_race_form"));
		assertThrows(ElementNotFoundException.class,
				() -> page.getFormByName("add_pilot_form"));
		final HtmlForm form = page.getFormByName("race_form");

		assertTrue(form.getInputByName("name").getValue().isEmpty());
		assertThat(page.getBody().getTextContent())
				.contains("No Pilots");
		form.getInputByName("name")
				.setValue("A name");
		form.getSelectByName("circuit.id").getOptionByText("second circuit")
				.setSelected(true);
		form.getButtonByName("btn_edit_name").click();

		ArgumentCaptor<Race> raceCaptor = ArgumentCaptor.forClass(Race.class);
		verify(service).insertNewRace(raceCaptor.capture());

		Race race = raceCaptor.getValue();
		assertEquals(2L, race.getCircuit().getId());
		assertEquals("A name", race.getName());
		assertThat(race.getPilotsList()).isEmpty();
	}
}
