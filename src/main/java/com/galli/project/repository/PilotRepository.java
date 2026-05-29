package com.galli.project.repository;

import java.util.List;
import java.util.Optional;

import com.galli.project.model.Pilot;

public interface PilotRepository {

	public List<Pilot> findAll();
	
	public Optional<Pilot> findById(long id);
	
	public Pilot save(Pilot pilot);
}
