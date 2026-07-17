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

import com.galli.project.model.Circuit;

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
class CircuitCompleteIT {

	@Autowired
	private MockMvc mvc;
	@Autowired
	private TestEntityManager entityManager;

	@Test
	@DisplayName("Test view circuits")
	void test1() throws Exception {
		long id = (long) entityManager
				.persistAndGetId(new Circuit("test", 1000L));

		List<Circuit> circuits = asList(new Circuit(id, "test", 1000L));

		mvc.perform(get("/circuits"))
				.andExpect(model().attribute("circuits", circuits));
	}

	@Test
	@DisplayName("Edit circuit page")
	void test2() throws Exception {
		long id = (long) entityManager
				.persistAndGetId(new Circuit("test", 1000L));

		Circuit circuit = new Circuit(id, "test", 1000L);

		mvc.perform(get("/circuits/edit/" + id))
				.andExpect(model().attribute("circuit", circuit));
	}

	@Test
	@DisplayName("Post circuit without id should insert new circuit")
	void test3() throws Exception {
		mvc.perform(post("/circuits/save").param("name", "test name"));

		assertFalse(
				entityManager.getEntityManager()
						.createQuery(
								"SELECT p FROM Circuit p WHERE p.name = 'test name'")
						.getResultList().isEmpty());
	}

	@Test
	@DisplayName("Post circuit with id should update existing circuit")
	void test4() throws Exception {
		long id = (long) entityManager
				.persistAndGetId(new Circuit("old name", 1000L));

		mvc.perform(post("/circuits/save").param("id", ""
				+ id).param("name",
						"new name")
				.param("length",
						"2000"));

		assertThat(entityManager.find(Circuit.class, id).getName())
				.isEqualTo("new name");
		assertThat(entityManager.find(Circuit.class, id).getLength())
				.isEqualTo(2000L);
	}

	@Test
	@DisplayName("Test delete")
	void test5() throws Exception {
		long id = (long) entityManager
				.persistAndGetId(new Circuit("to be deleted", 1000L));

		mvc.perform(post("/circuits/delete/" + id));

		assertNull(entityManager.find(Circuit.class, id));
	}
}
