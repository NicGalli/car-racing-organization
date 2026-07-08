package com.galli.project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.galli.project.model.Circuit;

@Repository
public class CircuitRepositoryTempImpl implements CircuitRepository {
	private static final String TEMPORARY_IMPLEMENTATION = "Temporary implementation";

	@Override
	public Circuit save(Circuit any) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	@Override
	public void deleteById(long l) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	@Override
	public List<Circuit> findAllByOrderByIdAsc() {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	@Override
	public Optional<Circuit> findById(long l) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

}
