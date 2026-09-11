package com.galli.project.service;

import java.util.List;

import com.galli.project.model.Circuit;
import com.galli.project.model.Race;
import com.galli.project.repository.CircuitRepository;
import com.galli.project.repository.RaceRepository;

public class RaceServiceImpl implements RaceService {

	private RaceRepository raceRepository;
	private CircuitRepository circuitRepository;

	public RaceServiceImpl(RaceRepository raceRepository,
			CircuitRepository circuitRepository) {
		this.raceRepository = raceRepository;
		this.circuitRepository = circuitRepository;
	}

	@Override
	public List<Race> getAllRaces() {
		return raceRepository.findAllByOrderByIdAsc();
	}

	@Override
	public Race getRaceById(int id) {
		return raceRepository.findById(id).orElse(null);
	}

	@Override
	public Race insertNewRace(Race race) {
		race.setId(null);
		return raceRepository.save(race);
	}

	@Override
	public Race updateRaceById(long id, Race replacement) {
		replacement.setId(id);
		return raceRepository.save(replacement);
	}

	@Override
	public void deleteRaceById(long id) {
		raceRepository.deleteById(id);
	}

	@Override
	public List<Circuit> getAllCircuits() {
		return circuitRepository.findAllByOrderByIdAsc();
	}

}
