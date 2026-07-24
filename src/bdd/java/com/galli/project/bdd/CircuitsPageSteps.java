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

import com.galli.project.model.Circuit;
import com.galli.project.repository.CircuitRepository;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CircuitsPageSteps extends CucumberSpringConfig {
	@Autowired
	private WebClient webClient;
	@Autowired
	private TestEntityManager entityManager;
	@Autowired
	private CircuitRepository repository;

	private HtmlPage circuitListPage;
	private HtmlPage editCircuitPage;
	private List<Long> ids;

	@Before
	@After
	public void clean() {
		ids = null;
		circuitListPage = null;
		editCircuitPage = null;
		repository.deleteAll();
		repository.flush();
	}

	@Given("The database contains a few circuits")
	public void the_database_contains_a_few_circuits() {
		ids = asList(
				(long) entityManager
						.persistAndGetId(new Circuit("first circuit", 1000L)),
				(long) entityManager
						.persistAndGetId(new Circuit("second circuit", 2000L)),
				(long) entityManager
						.persistAndGetId(new Circuit("third circuit", 3000L)));

	}

	@Given("The Circuits Page is requested")
	public void the_circuits_page_is_requested() throws IOException {
		circuitListPage = webClient.getPage("/circuits");
	}

	@Given("The Circuits Page is shown")
	public void the_circuits_page_is_shown() {
		assertThat(circuitListPage.getTitleText()).isEqualTo("Circuits");
	}

	@Given("The list contains a few circuits")
	public void the_list_contains_a_few_circuits() {
		assertThat(circuitListPage.getBody().getTextContent())
				.doesNotContain("No Circuits");

		HtmlTable table = circuitListPage.getHtmlElementById("circuits_table");

		String textBlock = """
				Circuits
				ID	Name	Length (m)
				%d	first circuit	1000	Edit
				%d	second circuit	2000	Edit
				%d	third circuit	3000	Edit""".formatted(
				ids.get(0),
				ids.get(1),
				ids.get(2));
		assertThat(table.asNormalizedText()).isEqualTo(textBlock);
		circuitListPage.getAnchorByHref("/circuits/edit/" + ids.get(0));
		circuitListPage.getAnchorByHref("/circuits/edit/" + ids.get(1));
		circuitListPage.getAnchorByHref("/circuits/edit/" + ids.get(2));
	}

	@Given("The user clicks the new circuit button")
	public void the_user_clicks_the_new_circuit_button() throws IOException {
		assertThat(
				circuitListPage.getAnchorByText("New Circuit")
						.getHrefAttribute())
				.isEqualTo("/circuits/new");
		editCircuitPage = circuitListPage.getAnchorByText("New Circuit")
				.click();
	}

	@Given("The user is redirected to the edit circuit page")
	public void the_user_is_redirected_to_the_edit_circuit_page() {
		assertThat(editCircuitPage
				.getElementsByTagName("h1")
				.getFirst()
				.getTextContent())
				.isEqualTo("Edit Circuit");
	}

	@When("The user fills the circuit form")
	public void the_user_fills_the_circuit_form() {
		HtmlForm form = editCircuitPage.getFormByName("circuit_form");
		form.getInputByName("name").setValueAttribute("new circuit");
		form.getInputByName("length").setValueAttribute("4000");
	}

	@When("The user clicks the confirm button of the edit circuit page")
	public void the_user_clicks_the_confirm_button_of_the_edit_circuit_page()
			throws IOException {
		HtmlForm form = editCircuitPage.getFormByName("circuit_form");
		circuitListPage = form.getButtonByName("btn_submit").click();
	}

	@Given("The user clicks the edit circuit button")
	public void the_user_clicks_the_edit_circuit_button() throws IOException {
		editCircuitPage = circuitListPage
				.getAnchorByHref("/circuits/edit/" + ids.get(0)).click();
	}

	@Then("The list contains a few circuits and the updated circuit")
	public void the_list_contains_a_few_circuits_and_the_updated_circuit() {
		assertThat(circuitListPage.getBody().getTextContent())
				.doesNotContain("No Circuits");

		HtmlTable table = circuitListPage.getHtmlElementById("circuits_table");

		String textBlock = """
				Circuits
				ID	Name	Length (m)
				%d	new circuit	4000	Edit
				%d	second circuit	2000	Edit
				%d	third circuit	3000	Edit""".formatted(
				ids.get(0),
				ids.get(1),
				ids.get(2));
		assertThat(table.asNormalizedText()).isEqualTo(textBlock);
		circuitListPage.getAnchorByHref("/circuits/edit/" + ids.get(0));
		circuitListPage.getAnchorByHref("/circuits/edit/" + ids.get(1));
		circuitListPage.getAnchorByHref("/circuits/edit/" + ids.get(2));
	}

	@When("The user clicks the delete circuit button")
	public void the_user_clicks_the_delete_circuit_button() throws IOException {
		HtmlForm form = editCircuitPage.getFormByName("circuit_form");
		circuitListPage = form.getButtonByName("btn_delete").click();
	}

	@Then("The circuit is not shown in the list")
	public void the_circuit_is_not_shown_in_the_list() {
		assertThat(circuitListPage.getBody().getTextContent())
				.doesNotContain("No Circuits");

		HtmlTable table = circuitListPage.getHtmlElementById("circuits_table");

		String textBlock = """
				Circuits
				ID	Name	Length (m)
				%d	second circuit	2000	Edit
				%d	third circuit	3000	Edit""".formatted(
				ids.get(1),
				ids.get(2));
		assertThat(table.asNormalizedText()).isEqualTo(textBlock);
		circuitListPage.getAnchorByHref("/circuits/edit/" + ids.get(1));
		circuitListPage.getAnchorByHref("/circuits/edit/" + ids.get(2));
	}

	@Then("The circuit is not present in the database")
	public void the_circuit_is_not_present_in_the_database() {
		assertNull(entityManager.find(Circuit.class, ids.get(0)));
	}

	@Then("The list contains a few circuits and the new circuit")
	public void the_list_contains_a_few_circuits_and_the_new_circuit() {
		assertThat(circuitListPage.getBody().getTextContent())
				.doesNotContain("No Circuits");

		HtmlTable table = circuitListPage.getHtmlElementById("circuits_table");
		long newId = repository.findByName("new circuit").getFirst().getId();
		String textBlock = """
				Circuits
				ID	Name	Length (m)
				%d	first circuit	1000	Edit
				%d	second circuit	2000	Edit
				%d	third circuit	3000	Edit
				%d	new circuit	4000	Edit""".formatted(
				ids.get(0),
				ids.get(1),
				ids.get(2), newId);

		assertThat(table.asNormalizedText()).isEqualTo(textBlock);
		circuitListPage.getAnchorByHref("/circuits/edit/" + ids.get(0));
		circuitListPage.getAnchorByHref("/circuits/edit/" + ids.get(1));
		circuitListPage.getAnchorByHref("/circuits/edit/" + ids.get(2));
		circuitListPage.getAnchorByHref("/circuits/edit/" + newId);
	}
}