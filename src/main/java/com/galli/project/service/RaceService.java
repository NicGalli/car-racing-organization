package com.galli.project.service;

import java.util.List;

import com.galli.project.model.Race;

public interface RaceService {

	public List<Race> getAllRaces();

	public Race getRaceById(int id);

	public Race insertNewRace(Race race);

	public Race updateRaceById(long id, Race replacement);

	public void deleteRaceById(long id);

}
