package com.galli.project.controller;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.html.HtmlTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
	void test7() throws Exception {
		HtmlPage page = webClient.getPage("/races");
		assertThat(page.getAnchorByText("New Race").getHrefAttribute())
				.isEqualTo("/races/new");
	}

}
