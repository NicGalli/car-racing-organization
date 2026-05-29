package com.galli.project.service;

import java.util.List;

import com.galli.project.model.Pilot;

public interface PilotService {

	public List<Pilot> getAllPilots();

	public Pilot getPilotById(long id);

	public Pilot insertNewPilot(Pilot pilot);

	public Pilot updatePilotById(long id, Pilot replacement);
}