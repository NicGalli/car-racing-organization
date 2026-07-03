package com.galli.project.bdd;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.web.servlet.MockMvc;

import com.galli.project.model.Pilot;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class PilotsPageSteps extends CucumberSpringConfig {

	@Autowired
	private MockMvc mvc;
	@Autowired
	private TestEntityManager entityManager;

	@Given("The database contains a few pilots")
	public void the_database_contains_a_few_pilots() throws Exception {
		entityManager.persist(new Pilot("first pilot"));
		entityManager.persist(new Pilot("second pilot"));
		entityManager.persist(new Pilot("third pilot"));
	}

	@Given("The Pilots Page is requested")
	public void the_pilots_page_is_requested() throws Exception {
		mvc.perform(get("/pilots"));
	}

	@Given("The Pilots Page is shown")
	public void the_pilots_page_is_shown() {
		// Write code here that turns the phrase above into concrete actions
		System.out.println("hola");

	}

	@Then("The list contains a few pilots")
	public void the_list_contains_a_few_pilots() {
		// Write code here that turns the phrase above into concrete actions
		System.out.println("hola");
	}

	@When("The user fills the pilot form")
	public void the_user_fills_the_pilot_form() {
		// Write code here that turns the phrase above into concrete actions
		System.out.println("hola");
	}

	@When("The user clicks the confirm button")
	public void the_user_clicks_the_confirm_button() {
		// Write code here that turns the phrase above into concrete actions
		System.out.println("hola");
	}

	@Then("The list contains the new pilot")
	public void the_list_contains_the_new_pilot() {
		// Write code here that turns the phrase above into concrete actions
		System.out.println("hola");
	}

	@Given("The pilot is shown in the list")
	public void thePilotIsShownInTheList() {
		System.out.println("hola");
	}

	@When("The user clicks the delete pilot button")
	public void theUserClicksTheDeletePilotButton() {
		System.out.println("hola");
	}

	@Then("The pilot is not shown in the list")
	public void thePilotIsNotShownInTheList() {
		System.out.println("hola");
	}

	@Then("The pilot is not present in the database")
	public void thePilotIsNotPresentInTheDatabase() {
		System.out.println("hola");
	}

	@Then("The list contains a few pilots and the updated pilot")
	public void theListContainsAFewPilotsAndTheUpdatedPilot() {
		System.out.println("hola");
	}

	@Given("The user clicks the new pilot button")
	public void theUserClicksTheNewPilotButton() {
		System.out.println("hola");
	}

	@Given("The user is redirected to the edit pilot page")
	public void theUserIsRedirectedToTheEditPilotPage() {
		System.out.println("hola");
	}

	@Given("The user clicks the edit pilot button")
	public void theUserClicksTheEditPilotButton() {
		System.out.println("hola");
	}
}
