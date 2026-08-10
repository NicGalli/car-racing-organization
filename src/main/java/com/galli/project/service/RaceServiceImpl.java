package com.galli.project.service;

import java.util.List;

import com.galli.project.model.Race;
import com.galli.project.repository.RaceRepository;

public class RaceServiceImpl implements RaceService {

	private RaceRepository repository;

	public RaceServiceImpl(RaceRepository repository) {
		this.repository = repository;
	}

	@Override
	public List<Race> getAllRaces() {
		return repository.findAllByOrderByIdAsc();
	}

	@Override
	public Race getRaceById(int id) {
		return repository.findById(id).orElse(null);
	}

	@Override
	public Race insertNewRace(Race race) {
		race.setId(null);
		return repository.save(race);
	}

	@Override
	public Race updateRaceById(long id, Race replacement) {
		replacement.setId(id);
		return repository.save(replacement);
	}

	@Override
	public void deleteRaceById(long id) {
		repository.deleteById(id);
	}

}
