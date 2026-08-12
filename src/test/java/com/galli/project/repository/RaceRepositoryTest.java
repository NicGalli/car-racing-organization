package com.galli.project.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.annotation.DirtiesContext;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import com.galli.project.model.Circuit;
import com.galli.project.model.Pilot;
import com.galli.project.model.Race;

@DataJpaTest
@Testcontainers
@DirtiesContext
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RaceRepositoryTest {

	@Container
	@ServiceConnection
	static final PostgreSQLContainer postgres = new PostgreSQLContainer(
			"postgres:18-alpine");
	@Autowired
	private RaceRepository repository;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	@DisplayName("Test repository can save")
	void test1() {
		Circuit circuit = new Circuit();
		entityManager.persistAndFlush(circuit);

		Pilot pilot = new Pilot();
		entityManager.persistAndFlush(pilot);

		Set<Pilot> pilots = new HashSet<>();
		pilots.add(pilot);

		Race race = new Race(null, "test", circuit, pilots);

		Race saved = repository.save(race);

		Race retrieved = entityManager.find(Race.class, saved.getId());

		assertThat(retrieved).isEqualTo(saved);
	}

	@Test
	@DisplayName("Test repository can read from db")
	void test2() {
		Circuit circuit = new Circuit();
		entityManager.persistAndFlush(circuit);

		Pilot pilot = new Pilot();
		entityManager.persistAndFlush(pilot);

		Set<Pilot> pilots = new HashSet<>();
		pilots.add(pilot);

		Race race = new Race(null, "test", circuit, pilots);

		Race saved = entityManager.persistFlushFind(race);

		Race retrieved = repository.findById(saved.getId()).get();

		assertThat(retrieved).isEqualTo(saved);
	}

	@Test
	@DisplayName("Test adding to Race non persisted Circuit throws")
	void test3() {
		Circuit circuit = new Circuit();

		Race race = new Race(null, "test", circuit, null);

		assertThrows(IllegalStateException.class,
				() -> entityManager.persistAndFlush(race));
	}

	@Test
	@DisplayName("Test adding to Race non persisted Pilot throws")
	void test4() {
		Pilot pilot = new Pilot();
		Set<Pilot> pilots = new HashSet<>();
		pilots.add(pilot);

		Race race = new Race(null, "test", null, pilots);

		assertThrows(IllegalStateException.class,
				() -> entityManager.persistAndFlush(race));
	}

	@Test
	@DisplayName("Test deleting Circuit sets value to null inside Race")
	void test5() {
		Circuit circuit = new Circuit();
		entityManager.persistAndFlush(circuit);

		Pilot pilot = new Pilot();
		entityManager.persistAndFlush(pilot);
		Set<Pilot> pilots = new HashSet<>();
		pilots.add(pilot);

		Race race = new Race(null, "test", circuit, pilots);

		Race saved = entityManager.persistFlushFind(race);
		entityManager.detach(saved);

		entityManager.remove(circuit);
		entityManager.flush();
		entityManager.clear();

		assertNull(entityManager.find(Race.class, saved.getId()).getCircuit());
	}

	@Test
	@DisplayName("Test deleting a pilot removes such pilot inside Race")
	void test6() {
		Circuit circuit = new Circuit();
		entityManager.persistAndFlush(circuit);

		Pilot pilot = new Pilot();
		entityManager.persistAndFlush(pilot);
		Set<Pilot> pilots = new HashSet<>();
		pilots.add(pilot);

		Race race = new Race(null, "test", circuit, pilots);
		Race saved = entityManager.persistFlushFind(race);
		entityManager.detach(saved);

		entityManager.remove(pilot);
		entityManager.flush();
		entityManager.clear();

		assertThat(
				entityManager.find(Race.class, saved.getId()).getPilots())
				.isEmpty();
	}

	@Test
	@DisplayName("Test updating Circuit updates Circuit inside Race")
	void test7() {
		Circuit circuit = new Circuit("name", 1000L);
		entityManager.persistAndFlush(circuit);

		Pilot pilot = new Pilot();
		entityManager.persistAndFlush(pilot);
		Set<Pilot> pilots = new HashSet<>();
		pilots.add(pilot);

		Race race = new Race(null, "test", circuit, pilots);
		Race savedRace = entityManager.persistFlushFind(race);
		entityManager.detach(savedRace);

		circuit.setName("changed");
		circuit.setLength(2000L);
		Circuit savedCircuit = entityManager.persistFlushFind(circuit);
		entityManager.flush();
		entityManager.clear();

		assertEquals(savedCircuit, entityManager.find(Race.class,
				savedRace.getId()).getCircuit());
	}

	@Test
	@DisplayName("Test updating Pilot updates Pilot inside Race")
	void test8() {
		Circuit circuit = new Circuit();
		entityManager.persistAndFlush(circuit);

		Pilot pilot = new Pilot("John");
		entityManager.persistAndFlush(pilot);
		Set<Pilot> pilots = new HashSet<>();
		pilots.add(pilot);

		Race race = new Race(null, "test", circuit, pilots);
		Race savedRace = entityManager.persistFlushFind(race);
		entityManager.detach(savedRace);

		pilot.setName("Jim");
		Pilot savedPilot = entityManager.persistFlushFind(pilot);
		entityManager.flush();
		entityManager.clear();

		assertEquals(savedPilot, entityManager.find(Race.class,
				savedRace.getId()).getPilots().stream().findFirst().get());
	}
}
