package com.galli.project;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;

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
import com.galli.project.model.Pilot;
import com.galli.project.model.Race;
import com.galli.project.service.RaceService;
import com.galli.project.service.RaceServiceImpl;

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
@Import(RaceServiceImpl.class)
@DataJpaTest
@DirtiesContext
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RaceServiceRepositoryIT {

	@Autowired
	private RaceService service;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	@DisplayName("Test service can insert into repository")
	void test1() {
		Circuit circuit = entityManager
				.persistFlushFind(new Circuit("monza", 7000L));
		Pilot pilot1 = entityManager.persistFlushFind(new Pilot("first pilot"));
		Pilot pilot2 = entityManager
				.persistFlushFind(new Pilot("second pilot"));

		Race saved = service
				.insertNewRace(new Race("grand prix 1", circuit,
						Set.of(pilot1, pilot2)));
		assertThat(entityManager.find(Race.class, saved.getId())).isNotNull();
	}

	@Test
	@DisplayName("Test service can read from repository")
	void test2() {
		Circuit circuit = entityManager
				.persistFlushFind(new Circuit("monza", 7000L));
		Pilot pilot1 = entityManager.persistFlushFind(new Pilot("first pilot"));
		Pilot pilot2 = entityManager
				.persistFlushFind(new Pilot("second pilot"));

		long id1 = (long) entityManager
				.persistAndGetId(new Race("grand prix 1", circuit,
						Set.of(pilot1, pilot2)));
		long id2 = (long) entityManager
				.persistAndGetId(new Race("grand prix 2", null,
						null));

		assertThat(service.getRaceById(id1))
				.isEqualTo(new Race(id1, "grand prix 1", circuit,
						Set.of(pilot1, pilot2)));
		assertThat(service.getAllRaces()).containsExactly(
				new Race(id1, "grand prix 1", circuit,
						Set.of(pilot1, pilot2)),
				new Race(id2, "grand prix 2", null,
						null));
	}

	@Test
	@DisplayName("Test service can update into repository")
	void test3() {
		Circuit circuit1 = entityManager
				.persistFlushFind(new Circuit("monza", 7000L));
		Circuit circuit2 = entityManager
				.persistFlushFind(new Circuit("spa", 8000L));

		Pilot pilot1 = entityManager.persistFlushFind(new Pilot("first pilot"));
		Pilot pilot2 = entityManager
				.persistFlushFind(new Pilot("second pilot"));

		long id = (long) entityManager
				.persistAndGetId(new Race("grand prix 1", circuit1,
						new HashSet<>(asList(pilot1, pilot2))));

		Race updated = service.updateRaceById(id,
				new Race("grand prix 2", circuit2,
						new HashSet<>(asList(pilot1))));

		assertThat(entityManager.find(Race.class, id)).isEqualTo(updated);
	}

	@Test
	@DisplayName("Test service can delete from repository")
	void test4() {
		Circuit circuit1 = entityManager
				.persistFlushFind(new Circuit("monza", 7000L));

		Pilot pilot1 = entityManager.persistFlushFind(new Pilot("first pilot"));
		Pilot pilot2 = entityManager
				.persistFlushFind(new Pilot("second pilot"));

		long id = (long) entityManager
				.persistAndGetId(new Race("grand prix 1", circuit1,
						Set.of(pilot1, pilot2)));
		service.deleteRaceById(id);
		assertThat(entityManager.find(Race.class, id)).isNull();
	}

	@Test
	@DisplayName("Test getAllCircuits")
	void test5() {
		Circuit circuit1 = entityManager
				.persistFlushFind(new Circuit("monza", 7000L));
		Circuit circuit2 = entityManager
				.persistFlushFind(new Circuit("spa", 8000L));

		assertThat(service.getAllCircuits()).containsExactly(circuit1,
				circuit2);
	}

	@Test
	@DisplayName("Test getAllOtherPilots")
	void test6() {
		Pilot pilot1 = entityManager.persistFlushFind(new Pilot("first pilot"));
		Pilot pilot2 = entityManager
				.persistFlushFind(new Pilot("second pilot"));
		Pilot pilot3 = entityManager.persistFlushFind(new Pilot("third pilot"));
		Pilot pilot4 = entityManager
				.persistFlushFind(new Pilot("fourth pilot"));

		Circuit circuit1 = entityManager
				.persistFlushFind(new Circuit("monza", 7000L));

		long id = (long) entityManager
				.persistAndGetId(new Race("grand prix 1", circuit1,
						Set.of(pilot1, pilot2)));

		assertThat(service
				.getAllOtherPilots(id))
				.containsExactly(pilot3, pilot4);
	}

	@Test
	@DisplayName("Test addPilotToRaceById")
	void test7() {
		Pilot pilot1 = entityManager.persistFlushFind(new Pilot("first pilot"));
		Pilot pilot2 = entityManager
				.persistFlushFind(new Pilot("second pilot"));
		Pilot pilot3 = entityManager.persistFlushFind(new Pilot("third pilot"));

		Circuit circuit1 = entityManager
				.persistFlushFind(new Circuit("monza", 7000L));

		long id = (long) entityManager
				.persistAndGetId(new Race("grand prix 1", circuit1,
						new HashSet<>(asList(pilot1, pilot2))));

		assertThat(service
				.addPilotToRaceById(id, pilot3.getId()).getPilots())
				.contains(pilot1, pilot2, pilot3);
	}

	@Test
	@DisplayName("Test deletePilotFromRaceById")
	void test8() {
		Pilot pilot1 = entityManager.persistFlushFind(new Pilot("first pilot"));
		Pilot pilot2 = entityManager
				.persistFlushFind(new Pilot("second pilot"));

		Circuit circuit1 = entityManager
				.persistFlushFind(new Circuit("monza", 7000L));

		long id = (long) entityManager
				.persistAndGetId(new Race("grand prix 1", circuit1,
						new HashSet<>(asList(pilot1, pilot2))));

		assertThat(service
				.deletePilotFromRaceById(id, pilot2.getId()).getPilots())
				.containsExactly(pilot1);
	}
}
