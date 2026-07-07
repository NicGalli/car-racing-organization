package com.galli.project.service;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.galli.project.model.Pilot;
import com.galli.project.repository.PilotRepository;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = PilotServiceImpl.class)
class PilotServiceImplTest {

	@MockitoBean
	private PilotRepository pilotRepository;

	@Autowired
	private PilotServiceImpl pilotService;

	@Test
	@DisplayName("Test getAllPilots")
	void test1() {
		Pilot pilot1 = new Pilot(1L, "first");
		Pilot pilot2 = new Pilot(2L, "second");
		when(pilotRepository.findAllByOrderByIdAsc()).thenReturn(asList(pilot1, pilot2));
		assertThat(pilotService.getAllPilots()).containsExactly(pilot1, pilot2);
	}

	@Test
	@DisplayName("Test getPilotById if pilot is found")
	void test2() {
		Pilot pilot = new Pilot(1L, "pilot name");
		when(pilotRepository.findById(1L)).thenReturn(Optional.of(pilot));
		assertThat(pilotService.getPilotById(1)).isEqualTo(pilot);
	}

	@Test
	@DisplayName("Test getPilotById if pilot is not found")
	void test3() {
		when(pilotRepository.findById(anyLong())).thenReturn(Optional.empty());
		assertThat(pilotService.getPilotById(1)).isNull();
	}

	@Test
	@DisplayName("Test insertNewPilot")
	void test4() {
		Pilot toSave = spy(new Pilot(99L, "pilot name"));
		Pilot saved = new Pilot(1L, "saved");

		when(pilotRepository.save(any(Pilot.class))).thenReturn(saved);

		Pilot result = pilotService.insertNewPilot(toSave);
		assertThat(result).isSameAs(saved);

		InOrder inOrder = inOrder(toSave, pilotRepository);
		inOrder.verify(toSave).setId(null);
		inOrder.verify(pilotRepository).save(toSave);
	}

	@Test
	@DisplayName("Test updatePilotById")
	void test5() {
		Pilot replacement = spy(new Pilot(null, "pilot name"));
		Pilot updated = new Pilot(1L, "updated name");

		when(pilotRepository.save(any(Pilot.class))).thenReturn(updated);

		Pilot result = pilotService.updatePilotById(1L, replacement);
		assertThat(result).isSameAs(updated);

		InOrder inOrder = inOrder(replacement, pilotRepository);
		inOrder.verify(replacement).setId(1L);
		inOrder.verify(pilotRepository).save(replacement);
	}

	@Test
	@DisplayName("Test deletePilotById")
	void test6() {
		pilotService.deletePilotById(1L);
		verify(pilotRepository).deleteById(1L);
	}
}
