package com.galli.project.bdd;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.htmlunit.WebClient;
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

public class HomePageSteps extends CucumberSpringConfig {
	@Autowired
	private WebClient webClient;
	@Autowired
	private TestEntityManager testEntityManager;

	private HtmlPage races;
	private HtmlPage pilots;
	private HtmlPage circuits;
	private HtmlPage homepage;

	private List<Long> pilotIds;
	private List<Long> circuitIds;
	private List<Long> raceIds;

	@Given("The database contains a few races, pilots, and circuits")
	public void the_database_contains_a_few_races_pilots_and_circuits() {
		pilotIds = new ArrayList<>(asList(
				(long) testEntityManager
						.persistAndGetId(new Pilot("first pilot")),
				(long) testEntityManager
						.persistAndGetId(new Pilot("second pilot")),
				(long) testEntityManager
						.persistAndGetId(new Pilot("third pilot"))));

		circuitIds = new ArrayList<>(asList(
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
										new Circuit(circuitIds.get(0)),
										new HashSet<>(asList(
												new Pilot(pilotIds.get(0)),
												new Pilot(
														pilotIds.get(1)))))),
				(long) testEntityManager
						.persistAndGetId(
								new Race("second race",
										new Circuit(circuitIds.get(1)),
										new HashSet<>(asList(
												new Pilot(pilotIds.get(1)),
												new Pilot(
														pilotIds.get(2)))))),
				(long) testEntityManager
						.persistAndGetId(
								new Race("third race",
										new Circuit(circuitIds.get(2)),
										new HashSet<>(asList(
												new Pilot(
														pilotIds.get(1))))))));
	}

	@Then("The homepage is requested")
	public void the_homepage_is_requested() throws Exception {
		homepage = webClient.getPage("/homepage");
		assertThat(homepage.getTitleText()).isEqualTo("Homepage");
	}

	@Then("The homepage is shown, and it contains three links")
	public void the_homepage_is_shown_and_it_contains_three_links() {
		assertThat(homepage.getAnchorByText("Races List").getHrefAttribute())
				.isEqualTo("/races");
		assertThat(homepage.getAnchorByText("Pilots List").getHrefAttribute())
				.isEqualTo("/pilots");
		assertThat(homepage.getAnchorByText("Circuits List").getHrefAttribute())
				.isEqualTo("/circuits");
	}

	@When("The races page link is clicked")
	public void the_races_page_link_is_clicked() throws Exception {
		races = homepage.getAnchorByText("Races List").click();
	}

	@Then("The races list page is shown and it contains a few races")
	public void the_races_list_page_is_shown_and_it_contains_a_few_races() {
		assertThat(races.getBody().getTextContent())
				.doesNotContain("No Races");
		HtmlTable table = races.getHtmlElementById("races_table");
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

		races.getAnchorByHref("/races/view/" + raceIds.get(0));
		races.getAnchorByHref("/races/view/" + raceIds.get(1));
		races.getAnchorByHref("/races/view/" + raceIds.get(2));
	}

	@When("The pilots page link is clicked")
	public void the_pilots_page_link_is_clicked() throws Exception {
		pilots = homepage.getAnchorByText("Pilots List").click();
	}

	@Then("The pilots list page is shown and it contains a few pilots")
	public void the_pilots_list_page_is_shown_and_it_contains_a_few_pilots() {
		assertThat(pilots.getBody().getTextContent())
				.doesNotContain("No Pilots");

		HtmlTable table = pilots.getHtmlElementById("pilots_table");

		String textBlock = """
				Pilots
				ID	Name
				%d	first pilot	Edit
				%d	second pilot	Edit
				%d	third pilot	Edit""".formatted(
				pilotIds.get(0),
				pilotIds.get(1),
				pilotIds.get(2));
		assertThat(table.asNormalizedText()).isEqualTo(textBlock);
		pilots.getAnchorByHref("/pilots/edit/" + pilotIds.get(0));
		pilots.getAnchorByHref("/pilots/edit/" + pilotIds.get(1));
		pilots.getAnchorByHref("/pilots/edit/" + pilotIds.get(2));
	}

	@When("The circuits page link is clicked")
	public void the_circuits_page_link_is_clicked() throws Exception {
		circuits = homepage.getAnchorByText("Circuits List").click();
	}

	@Then("The circuits list page is shown and it contains a few circuits")
	public void the_circuits_list_page_is_shown_and_it_contains_a_few_circuits() {
		assertThat(circuits.getBody().getTextContent())
				.doesNotContain("No Circuits");

		HtmlTable table = circuits.getHtmlElementById("circuits_table");

		String textBlock = """
				Circuits
				ID	Name	Length (m)
				%d	first circuit	1000	Edit
				%d	second circuit	2000	Edit
				%d	third circuit	3000	Edit""".formatted(
				circuitIds.get(0),
				circuitIds.get(1),
				circuitIds.get(2));
		assertThat(table.asNormalizedText()).isEqualTo(textBlock);
		circuits.getAnchorByHref("/circuits/edit/" + circuitIds.get(0));
		circuits.getAnchorByHref("/circuits/edit/" + circuitIds.get(1));
		circuits.getAnchorByHref("/circuits/edit/" + circuitIds.get(2));
	}
}
