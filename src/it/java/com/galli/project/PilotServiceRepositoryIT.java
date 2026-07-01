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

import com.galli.project.model.Pilot;
import com.galli.project.service.PilotService;
import com.galli.project.service.PilotServiceImpl;

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
@Import(PilotServiceImpl.class)
@DataJpaTest
@DirtiesContext
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PilotServiceRepositoryIT {

	@Autowired
	private PilotService service;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	@DisplayName("Test service can insert into repository")
	void test1() {
		Pilot saved = service.insertNewPilot(new Pilot("mike"));
		assertThat(entityManager.find(Pilot.class, saved.getId())).isNotNull();
	}

	@Test
	@DisplayName("Test service can read from repository")
	void test2() {
		long id1 = (long) entityManager
				.persistAndGetId(new Pilot("first"));
		long id2 = (long) entityManager
				.persistAndGetId(new Pilot("second"));
		assertThat(service.getPilotById(id1))
				.isEqualTo(new Pilot(id1, "first"));
		assertThat(service.getAllPilots()).containsExactly(
				new Pilot(id1, "first"), new Pilot(id2, "second"));
	}

	@Test
	@DisplayName("Test service can update into repository")
	void test3() {
		long id = (long) entityManager
				.persistAndGetId(new Pilot("john"));
		Pilot updated = service.updatePilotById(id, new Pilot("mike"));
		assertThat(entityManager.find(Pilot.class, id)).isEqualTo(updated);
	}

	@Test
	@DisplayName("Test service can delete from repository")
	void test4() {
		long id = (long) entityManager
				.persistAndGetId(new Pilot("john"));
		service.deletePilotById(id);
		assertThat(entityManager.find(Pilot.class, id)).isNull();
	}

}
