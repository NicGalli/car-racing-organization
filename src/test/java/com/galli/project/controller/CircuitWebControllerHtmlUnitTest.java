package com.galli.project.controller;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlForm;
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
import com.galli.project.service.CircuitService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CircuitWebController.class)
class CircuitWebControllerHtmlUnitTest {

	@Autowired
	private WebClient webClient;

	@MockitoBean
	private CircuitService service;

	@Test
	@DisplayName("Test Circuits page title")
	void test1() throws Exception {
		HtmlPage page = webClient.getPage("/circuits");
		assertThat(page.getTitleText()).isEqualTo("Circuits");
	}

	@Test
	@DisplayName("Test Circuits page when there are no circuits")
	void test2() throws Exception {
		when(service.getAllCircuits()).thenReturn(emptyList());
		HtmlPage page = webClient.getPage("/circuits");
		assertThat(page.getBody().getTextContent()).contains("No Circuits");
	}

	@Test
	@DisplayName("Test Circuits page with circuits should show them in a table")
	void test3() throws Exception {
		when(service.getAllCircuits())
				.thenReturn(asList(new Circuit(1L, "test1", 1000L),
						new Circuit(2L, "test2", 2000L)));
		HtmlPage page = webClient.getPage("/circuits");
		assertThat(page.getBody().getTextContent())
				.doesNotContain("No Circuits");
		HtmlTable table = page.getHtmlElementById("circuits_table");
		String textBlock = """
				Circuits
				ID	Name	Length (m)
				1	test1	1000	Edit
				2	test2	2000	Edit""";
		assertThat(table.asNormalizedText()).isEqualTo(textBlock);
		page.getAnchorByHref("/circuits/edit/1");
		page.getAnchorByHref("/circuits/edit/2");
	}

	@Test
	@DisplayName("Test edit non existing circuit")
	void test4() throws Exception {
		when(service.getCircuitById(1L)).thenReturn(null);
		HtmlPage page = webClient.getPage("/circuits/edit/1");
		assertThat(page.getTitleText()).isEqualTo("Circuits");
		assertThat(page.getBody().getTextContent())
				.contains("No Circuit found with id 1");
	}

	@Test
	@DisplayName("Test edit existing circuit")
	void test5() throws Exception {
		when(service.getCircuitById(1L))
				.thenReturn(new Circuit(1L, "original", 1000L));
		HtmlPage page = webClient.getPage("circuits/edit/1");

		final HtmlForm form = page.getFormByName("circuit_form");
		form.getInputByValue("original")
				.setValueAttribute("modified");
		form.getButtonByName("btn_submit").click();

		verify(service).updateCircuitById(1L,
				new Circuit(1L, "modified", 1000L));
	}

	@Test
	@DisplayName("Test edit new Circuit")
	void test6() throws Exception {
		HtmlPage page = webClient.getPage("circuits/new");

		final HtmlForm form = page.getFormByName("circuit_form");
		form.getInputByName("name").setValueAttribute("new");
		form.getInputByName("length").setValueAttribute("1000");

		form.getButtonByName("btn_submit").click();

		verify(service)
				.insertNewCircuit(new Circuit(null, "new", 1000L));
	}

	@Test
	@DisplayName("Test circuits page should provide a link for creating a new circuit")
	void test7() throws Exception {
		HtmlPage page = webClient.getPage("/circuits");
		assertThat(page.getAnchorByText("New Circuit").getHrefAttribute())
				.isEqualTo("/circuits/new");
	}

	@Test
	@DisplayName("Test deleting existing circuit")
	void test8() throws Exception {
		when(service.getCircuitById(1L))
				.thenReturn(new Circuit(1L, "to be deleted", 1000L));
		HtmlPage page = webClient.getPage("circuits/edit/1");

		final HtmlForm form = page.getFormByName("circuit_form");
		form.getButtonByName("btn_delete").click();

		verify(service).deleteCircuitById(1L);
	}

}
