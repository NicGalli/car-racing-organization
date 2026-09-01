package com.galli.project.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.galli.project.model.Race;

@Service
public class RaceServiceTempImpl implements RaceService {
	private static final String TEMPORARY_IMPLEMENTATION = "Temporary implementation";

	@Override
	public List<Race> getAllRaces() {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	@Override
	public Race getRaceById(long id) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	@Override
	public Race insertNewRace(Race race) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	@Override
	public Race updateRaceById(Long id, Race race) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	@Override
	public void deleteRaceById(long id) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

}
