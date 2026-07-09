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

import com.galli.project.model.Circuit;
import com.galli.project.repository.CircuitRepository;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CircuitServiceImpl.class)
class CircuitServiceImplTest {

	@MockitoBean
	private CircuitRepository circuitRepository;

	@Autowired
	private CircuitServiceImpl circuitService;

	@Test
	@DisplayName("Test getAllCircuits")
	void test1() {
		Circuit circuit1 = new Circuit(1L, "first", 1000L);
		Circuit circuit2 = new Circuit(2L, "second", 2000L);
		when(circuitRepository.findAllByOrderByIdAsc())
				.thenReturn(asList(circuit1, circuit2));
		assertThat(circuitService.getAllCircuits()).containsExactly(circuit1,
				circuit2);
	}

	@Test
	@DisplayName("Test getCircuitById if circuit is found")
	void test2() {
		Circuit circuit = new Circuit(1L, "circuit name", 1000L);
		when(circuitRepository.findById(1L)).thenReturn(Optional.of(circuit));
		assertThat(circuitService.getCircuitById(1)).isEqualTo(circuit);
	}

	@Test
	@DisplayName("Test getCircuitById if circuit is not found")
	void test3() {
		when(circuitRepository.findById(anyLong()))
				.thenReturn(Optional.empty());
		assertThat(circuitService.getCircuitById(1)).isNull();
	}

	@Test
	@DisplayName("Test insertNewCircuit")
	void test4() {
		Circuit toSave = spy(new Circuit(99L, "circuit name", 1000L));
		Circuit saved = new Circuit(1L, "saved", 2000L);

		when(circuitRepository.save(any(Circuit.class))).thenReturn(saved);

		Circuit result = circuitService.insertNewCircuit(toSave);
		assertThat(result).isSameAs(saved);

		InOrder inOrder = inOrder(toSave, circuitRepository);
		inOrder.verify(toSave).setId(null);
		inOrder.verify(circuitRepository).save(toSave);
	}

	@Test
	@DisplayName("Test updateCircuitById")
	void test5() {
		Circuit replacement = spy(new Circuit(null, "circuit name", 2000L));
		Circuit updated = new Circuit(1L, "updated name", 1000L);

		when(circuitRepository.save(any(Circuit.class))).thenReturn(updated);

		Circuit result = circuitService.updateCircuitById(1L, replacement);
		assertThat(result).isSameAs(updated);

		InOrder inOrder = inOrder(replacement, circuitRepository);
		inOrder.verify(replacement).setId(1L);
		inOrder.verify(circuitRepository).save(replacement);
	}

	@Test
	@DisplayName("Test deleteCircuitById")
	void test6() {
		circuitService.deleteCircuitById(1L);
		verify(circuitRepository).deleteById(1L);
	}
}
