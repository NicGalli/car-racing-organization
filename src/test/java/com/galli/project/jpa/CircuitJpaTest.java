package com.galli.project.jpa;

import static org.assertj.core.api.Assertions.assertThat;

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

@DataJpaTest
@Testcontainers
@DirtiesContext
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CircuitJpaTest {

	@Container
	@ServiceConnection
	static final PostgreSQLContainer postgres = new PostgreSQLContainer(
			"postgres:18-alpine");

	@Autowired
	private TestEntityManager entityManager;

	@Test
	void testJpaMapping() {
		Circuit saved = entityManager
				.persistFlushFind(new Circuit(null, "test", 1000L));

		assertThat(saved.getName()).isEqualTo("test");
		assertThat(saved.getId()).isNotNull();
		assertThat(saved.getId()).isPositive();
	}
}
