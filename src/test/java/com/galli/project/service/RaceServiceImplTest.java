package com.galli.project.service;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.galli.project.model.Circuit;
import com.galli.project.model.Pilot;
import com.galli.project.model.Race;
import com.galli.project.repository.RaceRepository;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = RaceServiceImpl.class)
class RaceServiceImplTest {

	@MockitoBean
	private RaceRepository raceRepository;

	@Autowired
	private RaceServiceImpl raceService;

	@Test
	@DisplayName("Test getAllRaces")
	void test1() {
		Race race1 = new Race(1L, "first", new Circuit(),
				new HashSet<Pilot>());
		Race race2 = new Race(2L, "second", new Circuit(),
				new HashSet<Pilot>());
		when(raceRepository.findAllByOrderByIdAsc())
				.thenReturn(asList(race1, race2));
		assertThat(raceService.getAllRaces()).containsExactly(race1, race2);
	}

	@Test
	@DisplayName("Test getRaceById if Race is found")
	void test2() {
		Race race = new Race(1L, "Race name", new Circuit(),
				new HashSet<Pilot>());
		when(raceRepository.findById(1L)).thenReturn(Optional.of(race));
		assertThat(raceService.getRaceById(1)).isEqualTo(race);
	}

	@Test
	@DisplayName("Test getRaceById if Race is not found")
	void test3() {
		when(raceRepository.findById(anyLong())).thenReturn(Optional.empty());
		assertThat(raceService.getRaceById(1)).isNull();
	}

	@Test
	@DisplayName("Test insertNewRace")
	void test4() {
		Race toSave = spy(new Race(99L, "Race name", new Circuit(),
				new HashSet<Pilot>()));
		Race saved = new Race(1L, "saved", new Circuit(),
				new HashSet<Pilot>());

		when(raceRepository.save(any(Race.class))).thenReturn(saved);

		Race result = raceService.insertNewRace(toSave);
		assertThat(result).isSameAs(saved);

		InOrder inOrder = inOrder(toSave, raceRepository);
		inOrder.verify(toSave).setId(null);
		inOrder.verify(raceRepository).save(toSave);
	}

	@Test
	@DisplayName("Test updateRaceById")
	void test5() {
		Race replacement = spy(new Race(null, "Race name", new Circuit(),
				new HashSet<Pilot>()));
		Race updated = new Race(1L, "updated name", new Circuit(),
				new HashSet<Pilot>());

		when(raceRepository.save(any(Race.class))).thenReturn(updated);

		Race result = raceService.updateRaceById(1L, replacement);
		assertThat(result).isSameAs(updated);

		InOrder inOrder = inOrder(replacement, raceRepository);
		inOrder.verify(replacement).setId(1L);
		inOrder.verify(raceRepository).save(replacement);
	}

	@Test
	@DisplayName("Test deleteRaceById")
	void test6() {
		raceService.deleteRaceById(1L);
		verify(raceRepository).deleteById(1L);
	}

}
