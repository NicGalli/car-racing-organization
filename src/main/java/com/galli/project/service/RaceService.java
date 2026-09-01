package com.galli.project.service;

import java.util.List;

import com.galli.project.model.Race;

public interface RaceService {

	public List<Race> getAllRaces();

	public Race getRaceById(long id);

	public Race insertNewRace(Race race);

	public Race updateRaceById(Long id, Race race);

	public void deleteRaceById(long id);

}
