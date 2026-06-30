package com.galli.project.controller;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

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

import com.galli.project.model.Pilot;
import com.galli.project.service.PilotService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = PilotWebController.class)
class PilotWebControllerHtmlUnitTest {

	@Autowired
	private WebClient webClient;

	@MockitoBean
	private PilotService service;

	@Test
	@DisplayName("Test Pilots page title")
	void test1() throws Exception {
		HtmlPage page = webClient.getPage("/pilots");
		assertThat(page.getTitleText()).isEqualTo("Pilots");
	}

	@Test
	@DisplayName("Test Pilots page when there are no employees")
	void test2() throws Exception {
		when(service.getAllPilots()).thenReturn(emptyList());
		HtmlPage page = webClient.getPage("/pilots");
		assertThat(page.getBody().getTextContent()).contains("No Pilots");
	}

	@Test
	@DisplayName("Test Pilots page with pilots should show them in a table")
	void test3() throws Exception {
		when(service.getAllPilots())
				.thenReturn(asList(new Pilot(1L, "test1"),
						new Pilot(2L, "test2")));
		HtmlPage page = webClient.getPage("/pilots");
		assertThat(page.getBody().getTextContent())
				.doesNotContain("No Pilots");
		HtmlTable table = page.getHtmlElementById("pilots_table");
		assertThat(table.asNormalizedText()).isEqualTo("Pilots\n"
				+ "ID	Name\n" + "1	test1	Edit\n"
				+ "2	test2	Edit");
		page.getAnchorByHref("/pilots/edit/1");
		page.getAnchorByHref("/pilots/edit/2");
	}

}
