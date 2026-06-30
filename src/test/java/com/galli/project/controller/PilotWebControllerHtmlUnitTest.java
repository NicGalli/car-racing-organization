package com.galli.project.controller;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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

}
