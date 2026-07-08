package com.galli.project.service;

import java.util.List;

import com.galli.project.model.Circuit;

public interface CircuitService {

	public List<Circuit> getAllCircuits();

	public Circuit getCircuitById(long id);

	public Circuit insertNewCircuit(Circuit circuit);

	public Circuit updateCircuitById(long id, Circuit replacement);

	public void deleteCircuitById(long id);

}
