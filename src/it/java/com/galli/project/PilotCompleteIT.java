package com.galli.project;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jpa.test.autoconfigure.AutoConfigureTestEntityManager;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.galli.project.model.Pilot;

/**
 * Before executing this Test Case from Eclipse run the following command from
 * the folder where docker-compose.yml is in and wait a few seconds:
 * 
 * docker compose up -d postgres_db
 * 
 * After execution run this command from the same folder to remove the
 * container:
 * 
 * docker compose down
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DirtiesContext
@AutoConfigureTestEntityManager
class PilotCompleteIT {

	@Autowired
	private MockMvc mvc;
	@Autowired
	private TestEntityManager entityManager;

	@Test
	@DisplayName("Test view pilots")
	void test1() throws Exception {
		long id = (long) entityManager
				.persistAndGetId(new Pilot("test"));

		List<Pilot> pilots = asList(new Pilot(id, "test"));

		mvc.perform(get("/pilots"))
				.andExpect(model().attribute("pilots", pilots));
	}

	@Test
	@DisplayName("Edit pilot page")
	void test2() throws Exception {
		long id = (long) entityManager
				.persistAndGetId(new Pilot("test"));

		Pilot pilot = new Pilot(id, "test");

		mvc.perform(get("/pilots/edit/" + id))
				.andExpect(model().attribute("pilot", pilot));
	}

	@Test
	@DisplayName("Post pilot without id should insert new pilot")
	void test3() throws Exception {
		mvc.perform(post("/pilots/save").param("name", "test name"));

		assertFalse(
				entityManager.getEntityManager()
						.createQuery(
								"SELECT p FROM Pilot p WHERE p.name = 'test name'")
						.getResultList().isEmpty());
	}

	@Test
	@DisplayName("Post pilot with id should update existing pilot")
	void test4() throws Exception {
		long id = (long) entityManager.persistAndGetId(new Pilot("old name"));

		mvc.perform(post("/pilots/save").param("id", ""
				+ id).param("name",
						"new name"));

		assertThat(entityManager.find(Pilot.class, id).getName())
				.isEqualTo("new name");
	}

	@Test
	@DisplayName("Test delete")
	void test5() throws Exception {
		long id = (long) entityManager
				.persistAndGetId(new Pilot("to be deleted"));

		mvc.perform(post("/pilots/delete/" + id));

		assertNull(entityManager.find(Pilot.class, id));
	}
}
