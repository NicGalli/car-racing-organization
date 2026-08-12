package com.galli.project.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;

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
class RaceJpaTest {

	@Container
	@ServiceConnection
	static final PostgreSQLContainer postgres = new PostgreSQLContainer(
			"postgres:18-alpine");

	@Autowired
	private TestEntityManager entityManager;

	@Test
	void testJpaMapping() {
		Circuit circuit = new Circuit();
		entityManager.persistAndFlush(circuit);
		
		Pilot pilot = new Pilot();
		entityManager.persistAndFlush(pilot);
		
		Set<Pilot> pilots = new HashSet<>();
		pilots.add(pilot);

		Race saved = entityManager
				.persistFlushFind(new Race(null, "test", circuit, pilots));

		assertThat(saved.getName()).isEqualTo("test");
		assertThat(saved.getId()).isNotNull();
		assertThat(saved.getId()).isPositive();
		assertThat(saved.getCircuit()).isEqualTo(circuit);
		assertThat(saved.getPilots()).isEqualTo(pilots);
	}
}
