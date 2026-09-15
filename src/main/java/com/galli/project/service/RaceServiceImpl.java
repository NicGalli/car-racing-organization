package com.galli.project.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.galli.project.model.Circuit;
import com.galli.project.model.Pilot;
import com.galli.project.model.Race;
import com.galli.project.repository.CircuitRepository;
import com.galli.project.repository.PilotRepository;
import com.galli.project.repository.RaceRepository;

@Service
public class RaceServiceImpl implements RaceService {

	private RaceRepository raceRepository;
	private CircuitRepository circuitRepository;
	private PilotRepository pilotRepository;

	public RaceServiceImpl(RaceRepository raceRepository,
			CircuitRepository circuitRepository,
			PilotRepository pilotRepository) {
		this.raceRepository = raceRepository;
		this.circuitRepository = circuitRepository;
		this.pilotRepository = pilotRepository;
	}

	@Override
	public List<Race> getAllRaces() {
		return raceRepository.findAllByOrderByIdAsc();
	}

	@Override
	public Race getRaceById(Long id) {
		return raceRepository.findById(id).orElse(null);
	}

	@Override
	public Race insertNewRace(Race race) {
		race.setId(null);
		return raceRepository.save(race);
	}

	@Override
	public Race updateRaceById(Long id, Race replacement) {
		replacement.setId(id);
		return raceRepository.save(replacement);
	}

	@Override
	public void deleteRaceById(Long id) {
		raceRepository.deleteById(id);
	}

	@Override
	public List<Circuit> getAllCircuits() {
		return circuitRepository.findAllByOrderByIdAsc();
	}

	@Override
	public List<Pilot> getAllOtherPilots(Long raceId) {
		List<Pilot> racePilots = new ArrayList<>(
				raceRepository.findById(raceId).get()
						.getPilots());
		List<Pilot> otherPilots = new ArrayList<>(
				pilotRepository.findAllByOrderByIdAsc());
		otherPilots.removeAll(racePilots);
		return otherPilots;
	}

	@Override
	public Race addPilotToRaceById(Long raceId, Long pilotId) {
		Race race = raceRepository.findById(raceId).get();
		Pilot pilot = pilotRepository.findById(pilotId).get();
		race.getPilots().add(pilot);
		return raceRepository.save(race);
	}

	@Override
	public Race deletePilotFromRaceById(Long raceId, Long pilotId) {
		Race race = raceRepository.findById(raceId).get();
		Pilot pilot = pilotRepository.findById(pilotId).get();
		race.getPilots().remove(pilot);
		return raceRepository.save(race);
	}

}
