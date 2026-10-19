package com.galli.project;

import static org.assertj.core.api.Assertions.assertThat;

import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.galli.project.service.CircuitService;
import com.galli.project.service.PilotService;
import com.galli.project.service.RaceService;

@WebMvcTest
@MockitoBean(types = { RaceService.class, PilotService.class,
		CircuitService.class })
class HomepageIT {

	@Autowired
	private WebClient webClient;

	@Test
	@DisplayName("Test links in homepage lead to correct pages")
	void test1() throws Exception {
		HtmlPage homepage = webClient.getPage("/homepage");

		HtmlPage races = homepage.getAnchorByText("Races List").click();
		HtmlPage pilots = homepage.getAnchorByText("Pilots List").click();
		HtmlPage circuits = homepage.getAnchorByText("Circuits List").click();

		assertThat(races.getTitleText())
				.isEqualTo("Races");
		assertThat(pilots.getTitleText())
				.isEqualTo("Pilots");
		assertThat(circuits.getTitleText())
				.isEqualTo("Circuits");
	}
}
