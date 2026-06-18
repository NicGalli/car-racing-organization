package com.galli.project.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

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

import com.galli.project.model.Pilot;

@DataJpaTest
@Testcontainers
@DirtiesContext
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PilotRepositoryTest {

	@Container
	@ServiceConnection
	static final PostgreSQLContainer postgres = new PostgreSQLContainer(
			"postgres:18-alpine");
	@Autowired
	private PilotRepository repository;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	@DisplayName("Test repository can save and read")
	void test1() {
		Pilot pilot = new Pilot(null, "test");
		Pilot saved = repository.save(pilot);
		Collection<Pilot> pilots = repository.findAll();
		assertThat(pilots).containsExactly(saved);
	}

	@Test
	@DisplayName("Test repository can read from db")
	void test2() {
		Pilot pilot = new Pilot(null, "test");
		Pilot saved = entityManager.persistFlushFind(pilot);
		Collection<Pilot> pilots = repository.findAll();
		assertThat(pilots).containsExactly(saved);
	}
}