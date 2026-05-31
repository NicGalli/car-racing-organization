package com.galli.project.service;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
		when(pilotRepository.findAll()).thenReturn(asList(pilot1, pilot2));
		assertThat(pilotService.getAllPilots()).containsExactly(pilot1, pilot2);
	}

}
