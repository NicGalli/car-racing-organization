package com.galli.project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.galli.project.model.Pilot;

/**
 * Dummy class, temporary implementation.
 */
@Repository
public class PilotRepositoryImpl implements PilotRepository {

	private static final String TEMPORARY_IMPLEMENTATION = "Temporary implementation";

	@Override
	public List<Pilot> findAll() {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	@Override
	public Optional<Pilot> findById(long id) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	@Override
	public Pilot save(Pilot pilot) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}
	public void hello() {
		System.out.println();
	}

}
