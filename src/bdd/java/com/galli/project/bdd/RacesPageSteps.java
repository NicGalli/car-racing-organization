package com.galli.project.bdd;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlButton;
import org.htmlunit.html.HtmlForm;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.html.HtmlTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import com.galli.project.model.Circuit;
import com.galli.project.model.Pilot;
import com.galli.project.model.Race;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class RacesPageSteps extends CucumberSpringConfig {

	@Autowired
	private WebClient webClient;
	@Autowired
	private TestEntityManager testEntityManager;

	private HtmlPage viewRacePage;
	private HtmlPage racesPage;

	private List<Long> pilotIds;
	private List<Long> circuitsIds;
	private List<Long> raceIds;

	@Given("The database contains a few races")
	public void the_database_contains_a_few_races() {
		pilotIds = new ArrayList<>(asList(
				(long) testEntityManager
						.persistAndGetId(new Pilot("first pilot")),
				(long) testEntityManager
						.persistAndGetId(new Pilot("second pilot")),
				(long) testEntityManager
						.persistAndGetId(new Pilot("third pilot"))));

		circuitsIds = new ArrayList<>(asList(
				(long) testEntityManager
						.persistAndGetId(new Circuit("first circuit", 1000L)),
				(long) testEntityManager
						.persistAndGetId(new Circuit("second circuit", 2000L)),
				(long) testEntityManager
						.persistAndGetId(new Circuit("third circuit", 3000L))));

		raceIds = new ArrayList<>(asList(
				(long) testEntityManager
						.persistAndGetId(
								new Race("first race",
										new Circuit(circuitsIds.get(0)),
										new HashSet<>(asList(
												new Pilot(pilotIds.get(0)),
												new Pilot(
														pilotIds.get(1)))))),
				(long) testEntityManager
						.persistAndGetId(
								new Race("second race",
										new Circuit(circuitsIds.get(1)),
										new HashSet<>(asList(
												new Pilot(pilotIds.get(1)),
												new Pilot(
														pilotIds.get(2)))))),
				(long) testEntityManager
						.persistAndGetId(
								new Race("third race",
										new Circuit(circuitsIds.get(2)),
										new HashSet<>(asList(
												new Pilot(
														pilotIds.get(1))))))));
	}

	@When("The Races Page is requested")
	public void the_races_page_is_requested() throws Exception {
		racesPage = webClient.getPage("/races");
		assertThat(racesPage.getTitleText()).isEqualTo("Races");
	}

	@Then("The Races Page is shown")
	public void the_races_page_is_shown() {
		assertThat(racesPage.getTitleText()).isEqualTo("Races");
	}

	@Then("The list contains a few races")
	public void the_list_contains_a_few_races() {
		assertThat(racesPage.getBody().getTextContent())
				.doesNotContain("No Races");
		HtmlTable table = racesPage.getHtmlElementById("races_table");
		String textBlock = """
				Races
				ID	Name
				%d	first race	View
				%d	second race	View
				%d	third race	View""".formatted(
				raceIds.get(0),
				raceIds.get(1),
				raceIds.get(2));
		assertThat(table.asNormalizedText()).isEqualTo(textBlock);

		racesPage.getAnchorByHref("/races/view/" + raceIds.get(0));
		racesPage.getAnchorByHref("/races/view/" + raceIds.get(1));
		racesPage.getAnchorByHref("/races/view/" + raceIds.get(2));
	}

	@When("The user clicks the view race button")
	public void the_user_clicks_the_view_race_button() throws Exception {
		viewRacePage = racesPage
				.getAnchorByHref("/races/view/" + raceIds.get(0)).click();
	}

	@Then("The user is redirected to the view race page")
	public void the_user_is_redirected_to_the_view_race_page() {
		assertThat(viewRacePage.getTitleText()).isEqualTo("Race View");
	}

	@Then("The Race contains all the information")
	public void the_race_contains_all_the_information() {

		HtmlTable table = viewRacePage.getHtmlElementById("pilots_table");
		String textBlock = """
				Pilots
				ID	Name
				%d	first pilot	Remove
				%d	second pilot	Remove""".formatted(
				pilotIds.get(0),
				pilotIds.get(1));
		assertThat(table.asNormalizedText()).isEqualTo(textBlock);

		final HtmlForm form = viewRacePage.getFormByName("race_form");
		form.getInputByValue("first race");
		assertEquals(
				form.getSelectByName("circuit.id").getSelectedOptions()
						.getFirst().getText(),
				"first circuit");
	}

	@Given("The user clicks the new race button")
	public void the_user_clicks_the_new_race_button() throws Exception {
		viewRacePage = racesPage.getAnchorByText("New Race").click();
	}

	@When("The user fills the race form")
	public void the_user_fills_the_race_form() {
		HtmlForm form = viewRacePage.getFormByName("race_form");

		form.getInputByName("name").setValueAttribute("new race");
		form.getSelectByName("circuit.id").getOptionByText("second circuit")
				.setSelected(true);
	}

	@When("The user clicks the confirm button of the edit race page")
	public void the_user_clicks_the_confirm_button_of_the_edit_race_page()
			throws Exception {
		HtmlForm form = viewRacePage.getFormByName("race_form");
		viewRacePage = form.getButtonByName("btn_edit_name").click();
	}

	@Then("The view race Page is shown")
	public void the_view_race_page_is_shown() {
		assertThat(viewRacePage.getTitleText()).isEqualTo("Race View");
		String url = viewRacePage.getUrl().toString();
		// retrieve the id through the URL, id is the only digit
		url = url.replaceAll("\\D+", "");
		raceIds.add(Long.valueOf(url));
	}

	@Then("The Race contains the information of the new race")
	public void the_race_contains_the_information_of_the_new_race() {
		final HtmlForm form = viewRacePage.getFormByName("race_form");
		form.getInputByValue("new race");
		assertEquals(
				form.getSelectByName("circuit.id").getSelectedOptions()
						.getFirst().getText(),
				"second circuit");
	}

	@Then("The list contains a few races and the new race")
	public void the_list_contains_a_few_races_and_the_new_race()
			throws Exception {
		racesPage = webClient.getPage("/races");
		assertThat(racesPage.getTitleText()).isEqualTo("Races");

		assertThat(racesPage.getBody().getTextContent())
				.doesNotContain("No Races");
		HtmlTable table = racesPage.getHtmlElementById("races_table");
		String textBlock = """
				Races
				ID	Name
				%d	first race	View
				%d	second race	View
				%d	third race	View
				%d	new race	View""".formatted(
				raceIds.get(0),
				raceIds.get(1),
				raceIds.get(2),
				raceIds.get(3));
		assertThat(table.asNormalizedText()).isEqualTo(textBlock);

		racesPage.getAnchorByHref("/races/view/" + raceIds.get(0));
		racesPage.getAnchorByHref("/races/view/" + raceIds.get(1));
		racesPage.getAnchorByHref("/races/view/" + raceIds.get(2));
		racesPage.getAnchorByHref("/races/view/" + raceIds.get(3));
	}

	@Then("The new race is present in the database")
	public void the_new_race_is_present_in_the_database() {
		assertNotNull(
				testEntityManager.find(Race.class, raceIds.get(3)));
	}

	@When("The user selects a pilot and clicks the add pilot button")
	public void the_user_selects_a_pilot_and_clicks_the_add_pilot_button()
			throws Exception {

		final HtmlForm form = viewRacePage.getFormByName("add_pilot_form");

		form.getSelectByName("pilotId").getOptionByText("third pilot")
				.setSelected(true);
		viewRacePage = form.getButtonByName("btn_add_pilot").click();

	}

	@When("The race contains the old pilots and the new pilot")
	public void the_race_contains_the_old_pilots_and_the_new_pilot() {
		HtmlTable table = viewRacePage.getHtmlElementById("pilots_table");

		assertThat(table.asNormalizedText().lines())
				.containsExactlyInAnyOrderElementsOf(asList("Pilots",
						"ID	Name",
						"%d	first pilot	Remove".formatted(pilotIds.get(0)),
						"%d	second pilot	Remove".formatted(pilotIds.get(1)),
						"%d	third pilot	Remove".formatted(pilotIds.get(2))));

		assertThat(viewRacePage.getTitleText()).isEqualTo("Race View");
	}

	@Then("The user clicks the remove pilot button")
	public void the_user_clicks_the_remove_pilot_button() throws Exception {
		HtmlButton removeButton = viewRacePage.getFirstByXPath(
				"//tr[td[normalize-space()='first pilot']]//button[@name='btn_remove_pilot']");
		viewRacePage = removeButton.click();
	}

	@Then("The race contains all the pilots except the removed one")
	public void the_race_contains_all_the_pilots_except_the_removed_one() {
		HtmlTable table = viewRacePage.getHtmlElementById("pilots_table");

		assertThat(table.asNormalizedText().lines())
				.containsExactlyInAnyOrderElementsOf(asList("Pilots",
						"ID	Name",
						"%d	second pilot	Remove".formatted(pilotIds.get(1)),
						"%d	third pilot	Remove".formatted(pilotIds.get(2))));
		assertThat(viewRacePage.getTitleText()).isEqualTo("Race View");
	}

	@Given("The user updates the race form")
	public void the_user_updates_the_race_form() {

		final HtmlForm form = viewRacePage.getFormByName("race_form");
		form.getInputByValue("first race")
				.setValue("modified race");
		assertEquals(
				form.getSelectByName("circuit.id").getSelectedOptions()
						.getFirst().getText(),
				"first circuit");
		form.getSelectByName("circuit.id").getOptionByText("second circuit")
				.setSelected(true);
	}

	@When("The race is updated")
	public void the_race_is_updated() {

		final HtmlForm form = viewRacePage.getFormByName("race_form");
		form.getInputByValue("modified race");
		assertEquals(
				form.getSelectByName("circuit.id").getSelectedOptions()
						.getFirst().getText(),
				"second circuit");
	}

	@Then("The list contains a few races and the updated race")
	public void the_list_contains_a_few_races_and_the_updated_race() {
		HtmlTable table = racesPage.getHtmlElementById("races_table");
		String textBlock = """
				Races
				ID	Name
				%d	modified race	View
				%d	second race	View
				%d	third race	View""".formatted(
				raceIds.get(0),
				raceIds.get(1),
				raceIds.get(2));
		assertThat(table.asNormalizedText()).isEqualTo(textBlock);

		racesPage.getAnchorByHref("/races/view/" + raceIds.get(0));
		racesPage.getAnchorByHref("/races/view/" + raceIds.get(1));
		racesPage.getAnchorByHref("/races/view/" + raceIds.get(2));
	}

	@When("The user clicks the delete race button")
	public void the_user_clicks_the_delete_race_button() throws Exception {
		final HtmlForm form = viewRacePage.getFormByName("delete_race_form");
		racesPage = form.getButtonByName("btn_delete_race").click();
	}

	@Then("The race is not shown in the list")
	public void the_race_is_not_shown_in_the_list() {
		HtmlTable table = racesPage.getHtmlElementById("races_table");
		String textBlock = """
				Races
				ID	Name
				%d	second race	View
				%d	third race	View""".formatted(
				raceIds.get(1),
				raceIds.get(2));
		assertThat(table.asNormalizedText()).isEqualTo(textBlock);

		racesPage.getAnchorByHref("/races/view/" + raceIds.get(1));
		racesPage.getAnchorByHref("/races/view/" + raceIds.get(2));
	}

	@Then("The race is not present in the database")
	public void the_race_is_not_present_in_the_database() {
		assertNull(testEntityManager.find(Race.class, raceIds.get(0)));
	}

}
