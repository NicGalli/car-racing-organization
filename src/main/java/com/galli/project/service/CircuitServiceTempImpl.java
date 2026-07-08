package com.galli.project.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.galli.project.model.Circuit;

@Service
public class CircuitServiceTempImpl implements CircuitService {

	private static final String TEMPORARY_IMPLEMENTATION = "Temporary implementation";

	@Override
	public List<Circuit> getAllCircuits() {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	@Override
	public Circuit getCircuitById(long id) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	@Override
	public Circuit insertNewCircuit(Circuit circuit) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	@Override
	public Circuit updateCircuitById(long id, Circuit replacement) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	@Override
	public void deleteCircuitById(long id) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

}
