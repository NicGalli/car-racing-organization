package com.galli.project;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import com.galli.project.model.Pilot;
import com.galli.project.model.Race;

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
class RaceCompleteIT {

	@Autowired
	private MockMvc mvc;
	@Autowired
	private TestEntityManager entityManager;

	@Test
	@DisplayName("Test races list")
	void test1() throws Exception {
		Circuit circuit = entityManager
				.persistFlushFind(new Circuit("monza", 7000L));
		Pilot pilot1 = entityManager.persistFlushFind(new Pilot("first pilot"));
		Pilot pilot2 = entityManager
				.persistFlushFind(new Pilot("second pilot"));
		Set<Pilot> pilots = new HashSet<>();
		pilots.add(pilot1);
		pilots.add(pilot2);
		long id = (long) entityManager
				.persistAndGetId(new Race("test", circuit, pilots));

		List<Race> races = asList(new Race(id, "test", circuit, pilots));

		mvc.perform(get("/races"))
				.andExpect(model().attribute("races", races));
	}

	@Test
	@DisplayName("View Race page")
	void test2() throws Exception {
		Circuit circuit = entityManager
				.persistFlushFind(new Circuit("monza", 7000L));
		Pilot pilot1 = entityManager.persistFlushFind(new Pilot("first pilot"));
		Pilot pilot2 = entityManager
				.persistFlushFind(new Pilot("second pilot"));
		Set<Pilot> pilots = new HashSet<>();
		pilots.add(pilot1);
		pilots.add(pilot2);
		long id = (long) entityManager
				.persistAndGetId(new Race("test", circuit, pilots));

		Race race = new Race(id, "test", circuit, pilots);

		mvc.perform(get("/races/view/" + id))
				.andExpect(model().attribute("race", race));
	}

	@Test
	@DisplayName("Post race without id should insert new race")
	void test3() throws Exception {
		Circuit circuit = entityManager
				.persistFlushFind(new Circuit("circuit", 1000L));

		mvc.perform(post("/races/save")
				.param("name", "test name")
				.param("circuit.id", circuit.getId().toString()));
		assertFalse(
				entityManager.getEntityManager()
						.createQuery(
								"SELECT r FROM Race r WHERE r.name = 'test name'")
						.getResultList().isEmpty());
	}

	@Test
	@DisplayName("Post race with id should update existing race")
	void test4() throws Exception {
		Circuit circuit = entityManager
				.persistFlushFind(new Circuit("monza", 7000L));
		Pilot pilot1 = entityManager.persistFlushFind(new Pilot("first pilot"));
		Pilot pilot2 = entityManager
				.persistFlushFind(new Pilot("second pilot"));
		Set<Pilot> pilots = new HashSet<>();
		pilots.add(pilot1);
		pilots.add(pilot2);
		long id = (long) entityManager
				.persistAndGetId(new Race("old name", null, null));

		mvc.perform(
				post("/races/save").param("id", ""
						+ id).param("name", "new name")
						.param("circuit.id", circuit.getId().toString())
						.param("pilots[0].id", pilot1.getId().toString())
						.param("pilots[1].id", pilot2.getId().toString()))
				.andExpect(view().name("redirect:/races/view/" + id));

		Race race = new Race(id, "new name", circuit, pilots);
		assertThat(entityManager.find(Race.class, id))
				.isEqualTo(race);
	}

	@Test
	@DisplayName("Test delete")
	void test5() throws Exception {
		Circuit circuit = entityManager
				.persistFlushFind(new Circuit("monza", 7000L));
		Pilot pilot1 = entityManager.persistFlushFind(new Pilot("first pilot"));
		Pilot pilot2 = entityManager
				.persistFlushFind(new Pilot("second pilot"));
		Set<Pilot> pilots = new HashSet<>();
		pilots.add(pilot1);
		pilots.add(pilot2);
		long id = (long) entityManager
				.persistAndGetId(new Race("to be deleted", circuit, pilots));

		mvc.perform(post("/races/delete/" + id));

		assertNull(entityManager.find(Race.class, id));
	}

	@Test
	@DisplayName("Test add a pilot")
	void test6() throws Exception {
		Circuit circuit = entityManager
				.persistFlushFind(new Circuit("monza", 7000L));
		Pilot pilot1 = entityManager.persistFlushFind(new Pilot("first pilot"));
		Pilot pilot2 = entityManager
				.persistFlushFind(new Pilot("second pilot"));
		Set<Pilot> pilots = new HashSet<>();
		pilots.add(pilot1);
		long id = (long) entityManager
				.persistAndGetId(new Race("race", circuit, pilots));

		mvc.perform(post("/races/" + id + "/pilots/add").param("pilotId",
				"" + pilot2.getId()))
				.andExpect(view().name("redirect:/races/view/" + id));
		assertTrue(entityManager.find(Race.class, id).getPilots()
				.contains(pilot2));
	}

	@Test
	@DisplayName("Test delete a pilot")
	void test7() throws Exception {
		Circuit circuit = entityManager
				.persistFlushFind(new Circuit("monza", 7000L));
		Pilot pilot1 = entityManager.persistFlushFind(new Pilot("first pilot"));
		Pilot pilot2 = entityManager
				.persistFlushFind(new Pilot("second pilot"));
		Set<Pilot> pilots = new HashSet<>();
		pilots.add(pilot1);
		pilots.add(pilot2);
		long id = (long) entityManager
				.persistAndGetId(new Race("race", circuit, pilots));

		mvc.perform(post("/races/" + id + "/pilots/delete/" + pilot2.getId()))
				.andExpect(view().name("redirect:/races/view/" + id));
		assertTrue(entityManager.find(Race.class, id).getPilots()
				.equals(new HashSet<Pilot>(asList(pilot1))));
	}

}
