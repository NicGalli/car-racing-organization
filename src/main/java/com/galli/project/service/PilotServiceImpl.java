package com.galli.project.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.galli.project.model.Pilot;
import com.galli.project.repository.PilotRepository;

@Service
public class PilotServiceImpl implements PilotService {

	private PilotRepository repository;

	public PilotServiceImpl(PilotRepository repository) {
		this.repository = repository;
	}

	@Override
	public List<Pilot> getAllPilots() {
		return repository.findAll();
	}

	@Override
	public Pilot getPilotById(long id) {
		return repository.findById(id).orElse(null);
	}

	@Override
	public Pilot insertNewPilot(Pilot pilot) {
		pilot.setId(null);
		return repository.save(pilot);
	}

	@Override
	public Pilot updatePilotById(long id, Pilot replacement) {
		replacement.setId(id);
		return repository.save(replacement);
	}

	@Override
	public void deletePilotById(long id) {
		repository.deleteById(id);
	}

}
