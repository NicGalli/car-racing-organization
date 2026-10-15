package com.galli.project.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = HomepageWebController.class)
class HomepageWebControllerTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private WebClient webClient;

	@Test
	@DisplayName("Test homepage has status 2xx")
	void test1() throws Exception {
		mvc.perform(get("/homepage")).andExpect(status().is2xxSuccessful());
	}

	@Test
	@DisplayName("Test return homepage")
	void test2() throws Exception {
		ModelAndViewAssert.assertViewName(
				mvc.perform(get("/homepage")).andReturn().getModelAndView(),
				"homepage");
	}

	@Test
	@DisplayName("Test homepage title")
	void test3() throws Exception {
		HtmlPage page = webClient.getPage("/homepage");
		assertThat(page.getTitleText()).isEqualTo("Homepage");
	}

	@Test
	@DisplayName("Test Races page with races should show them in a table")
	void test4() throws Exception {
		HtmlPage page = webClient.getPage("/homepage");

		assertThat(page.getAnchorByText("Races List").getHrefAttribute())
				.isEqualTo("/races");
		assertThat(page.getAnchorByText("Pilots List").getHrefAttribute())
				.isEqualTo("/pilots");
		assertThat(page.getAnchorByText("Circuits List").getHrefAttribute())
				.isEqualTo("/circuits");
	}

}
