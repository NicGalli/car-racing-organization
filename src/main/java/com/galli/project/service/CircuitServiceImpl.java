package com.galli.project.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.galli.project.model.Circuit;
import com.galli.project.repository.CircuitRepository;

@Service
public class CircuitServiceImpl implements CircuitService {

	private CircuitRepository repository;

	public CircuitServiceImpl(CircuitRepository repository) {
		this.repository = repository;
	}

	@Override
	public List<Circuit> getAllCircuits() {
		return repository.findAllByOrderByIdAsc();
	}

	@Override
	public Circuit getCircuitById(long id) {
		return repository.findById(id).orElse(null);
	}

	@Override
	public Circuit insertNewCircuit(Circuit circuit) {
		circuit.setId(null);
		return repository.save(circuit);
	}

	@Override
	public Circuit updateCircuitById(long id, Circuit replacement) {
		replacement.setId(id);
		return repository.save(replacement);
	}

	@Override
	public void deleteCircuitById(long id) {
		repository.deleteById(id);
	}

}
