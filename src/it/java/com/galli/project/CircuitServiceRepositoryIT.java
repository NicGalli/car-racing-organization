package com.galli.project;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.galli.project.model.Circuit;
import com.galli.project.service.CircuitService;
import com.galli.project.service.CircuitServiceImpl;

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
@ExtendWith(SpringExtension.class)
@Import(CircuitServiceImpl.class)
@DataJpaTest
@DirtiesContext
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CircuitServiceRepositoryIT {

	@Autowired
	private CircuitService service;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	@DisplayName("Test service can insert into repository")
	void test1() {
		Circuit saved = service.insertNewCircuit(new Circuit("track", 1000L));
		assertThat(entityManager.find(Circuit.class, saved.getId()))
				.isNotNull();
	}

	@Test
	@DisplayName("Test service can read from repository")
	void test2() {
		long id1 = (long) entityManager
				.persistAndGetId(new Circuit("first", 1000L));
		long id2 = (long) entityManager
				.persistAndGetId(new Circuit("second", 2000L));
		assertThat(service.getCircuitById(id1))
				.isEqualTo(new Circuit(id1, "first", 1000L));
		assertThat(service.getAllCircuits()).containsExactly(
				new Circuit(id1, "first", 1000L),
				new Circuit(id2, "second", 2000L));
	}

	@Test
	@DisplayName("Test service can update into repository")
	void test3() {
		long id = (long) entityManager
				.persistAndGetId(new Circuit("spa", 1000L));
		Circuit updated = service.updateCircuitById(id,
				new Circuit("monza", 2000L));
		assertThat(entityManager.find(Circuit.class, id)).isEqualTo(updated);
	}

	@Test
	@DisplayName("Test service can delete from repository")
	void test4() {
		long id = (long) entityManager
				.persistAndGetId(new Circuit("spa", 1000L));
		service.deleteCircuitById(id);
		assertThat(entityManager.find(Circuit.class, id)).isNull();
	}

}
