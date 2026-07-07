package com.galli.project.bdd;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;
import java.util.List;

import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlForm;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.html.HtmlTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import com.galli.project.model.Pilot;
import com.galli.project.repository.PilotRepository;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class PilotsPageSteps extends CucumberSpringConfig {

	@Autowired
	private WebClient webClient;
	@Autowired
	private TestEntityManager entityManager;
	@Autowired
	private PilotRepository repository;

	private HtmlPage pilotListPage;
	private HtmlPage editPilotPage;
	private List<Long> ids;

	@Before
	@After
	public void clean() {
		ids = null;
		pilotListPage = null;
		editPilotPage = null;
		repository.deleteAll();
		repository.flush();
	}

	@Given("The database contains a few pilots")
	public void the_database_contains_a_few_pilots() {
		ids = asList(
				(long) entityManager.persistAndGetId(new Pilot("first pilot")),
				(long) entityManager.persistAndGetId(new Pilot("second pilot")),
				(long) entityManager.persistAndGetId(new Pilot("third pilot")));
	}

	@Given("The Pilots Page is requested")
	public void the_pilots_page_is_requested() throws Exception {
		pilotListPage = webClient.getPage("/pilots");
	}

	@Given("The Pilots Page is shown")
	public void the_pilots_page_is_shown() {
		assertThat(pilotListPage.getTitleText()).isEqualTo("Pilots");
	}

	@Then("The list contains a few pilots")
	public void the_list_contains_a_few_pilots() {
		assertThat(pilotListPage.getBody().getTextContent())
				.doesNotContain("No Pilots");

		HtmlTable table = pilotListPage.getHtmlElementById("pilots_table");

		String textBlock = """
				Pilots
				ID	Name
				%d	first pilot	Edit
				%d	second pilot	Edit
				%d	third pilot	Edit""".formatted(
				ids.get(0),
				ids.get(1),
				ids.get(2));
		assertThat(table.asNormalizedText()).isEqualTo(textBlock);
		pilotListPage.getAnchorByHref("/pilots/edit/" + ids.get(0));
		pilotListPage.getAnchorByHref("/pilots/edit/" + ids.get(1));
		pilotListPage.getAnchorByHref("/pilots/edit/" + ids.get(2));
	}

	@When("The user fills the pilot form")
	public void the_user_fills_the_pilot_form() {
		HtmlForm form = editPilotPage.getFormByName("pilot_form");
		form.getInputByName("name").setValueAttribute("new pilot");
	}

	@When("The user clicks the confirm button")
	public void the_user_clicks_the_confirm_button() throws IOException {
		HtmlForm form = editPilotPage.getFormByName("pilot_form");
		pilotListPage = form.getButtonByName("btn_submit").click();
	}

	@Then("The list contains a few pilots and the new pilot")
	public void the_list_contains_a_few_pilots_and_the_new_pilot() {
		assertThat(pilotListPage.getBody().getTextContent())
				.doesNotContain("No Pilots");

		HtmlTable table = pilotListPage.getHtmlElementById("pilots_table");
		long newId = repository.findByName("new pilot").getFirst().getId();
		String textBlock = """
				Pilots
				ID	Name
				%d	first pilot	Edit
				%d	second pilot	Edit
				%d	third pilot	Edit
				%d	new pilot	Edit""".formatted(
				ids.get(0),
				ids.get(1),
				ids.get(2),
				newId);

		assertThat(table.asNormalizedText()).isEqualTo(textBlock);
		pilotListPage.getAnchorByHref("/pilots/edit/" + ids.get(0));
		pilotListPage.getAnchorByHref("/pilots/edit/" + ids.get(1));
		pilotListPage.getAnchorByHref("/pilots/edit/" + ids.get(2));
		pilotListPage.getAnchorByHref("/pilots/edit/" + newId);
	}

	@When("The user clicks the delete pilot button")
	public void the_user_clicks_the_delete_pilot_button() throws IOException {
		HtmlForm form = editPilotPage.getFormByName("pilot_form");
		pilotListPage = form.getButtonByName("btn_delete").click();
	}

	@Then("The pilot is not shown in the list")
	public void thePilotIsNotShownInTheList() {

		assertThat(pilotListPage.getBody().getTextContent())
				.doesNotContain("No Pilots");

		HtmlTable table = pilotListPage.getHtmlElementById("pilots_table");

		String textBlock = """
				Pilots
				ID	Name
				%d	second pilot	Edit
				%d	third pilot	Edit""".formatted(
				ids.get(1),
				ids.get(2));
		assertThat(table.asNormalizedText()).isEqualTo(textBlock);
		pilotListPage.getAnchorByHref("/pilots/edit/" + ids.get(1));
		pilotListPage.getAnchorByHref("/pilots/edit/" + ids.get(2));
	}

	@Then("The pilot is not present in the database")
	public void thePilotIsNotPresentInTheDatabase() {
		assertNull(entityManager.find(Pilot.class, ids.get(0)));
	}

	@Then("The list contains a few pilots and the updated pilot")
	public void theListContainsAFewPilotsAndTheUpdatedPilot() {
		assertThat(pilotListPage.getBody().getTextContent())
				.doesNotContain("No Pilots");

		HtmlTable table = pilotListPage.getHtmlElementById("pilots_table");

		String textBlock = """
				Pilots
				ID	Name
				%d	new pilot	Edit
				%d	second pilot	Edit
				%d	third pilot	Edit""".formatted(
				ids.get(0),
				ids.get(1),
				ids.get(2));
		assertThat(table.asNormalizedText()).isEqualTo(textBlock);
		pilotListPage.getAnchorByHref("/pilots/edit/" + ids.get(0));
		pilotListPage.getAnchorByHref("/pilots/edit/" + ids.get(1));
		pilotListPage.getAnchorByHref("/pilots/edit/" + ids.get(2));
	}

	@Given("The user clicks the new pilot button")
	public void theUserClicksTheNewPilotButton() throws IOException {
		assertThat(
				pilotListPage.getAnchorByText("New Pilot").getHrefAttribute())
				.isEqualTo("/pilots/new");
		editPilotPage = pilotListPage.getAnchorByText("New Pilot").click();
	}

	@Given("The user is redirected to the edit pilot page")
	public void theUserIsRedirectedToTheEditPilotPage() {
		assertThat(editPilotPage
				.getElementsByTagName("h1")
				.getFirst()
				.getTextContent())
				.isEqualTo("Edit Pilot");
	}

	@Given("The user clicks the edit pilot button")
	public void theUserClicksTheEditPilotButton() throws IOException {
		editPilotPage = pilotListPage
				.getAnchorByHref("/pilots/edit/" + ids.get(0)).click();
	}
}
