package com.galli.project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.galli.project.model.Race;

@Repository
public class RaceRepository {

	private static final String TEMPORARY_IMPLEMENTATION = "Temporary implementation";

	public List<Race> findAllByOrderByIdAsc() {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	public Optional<Race> findById(long id) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	public Race save(Race race) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	public void deleteById(long id) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

}
