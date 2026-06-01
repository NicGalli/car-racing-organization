package com.galli.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.galli.project.model.Pilot;
import com.galli.project.repository.PilotRepository;

@Service
public class PilotServiceImpl implements PilotService {

	@Autowired
	private PilotRepository repository;

	@Override
	public List<Pilot> getAllPilots() {
		return repository.findAll();
	}

	@Override
	public Pilot getPilotById(long id) {
		return repository.findById(id).get();
	}

	@Override
	public Pilot insertNewPilot(Pilot pilot) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pilot updatePilotById(long id, Pilot replacement) {
		// TODO Auto-generated method stub
		return null;
	}

}
