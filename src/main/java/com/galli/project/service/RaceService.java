package com.galli.project.service;

import java.util.List;

import com.galli.project.model.Circuit;
import com.galli.project.model.Pilot;
import com.galli.project.model.Race;

public interface RaceService {

	public List<Race> getAllRaces();

	public Race getRaceById(Long id);

	public Race insertNewRace(Race race);

	public Race updateRaceById(Long id, Race replacement);

	public void deleteRaceById(Long id);

	public List<Circuit> getAllCircuits();

	public List<Pilot> getAllOtherPilots(Long raceId);

	public Race addPilotToRaceById(Long raceId, Long pilotId);

	public Race deletePilotFromRaceById(Long raceId, Long pilotId);

}
