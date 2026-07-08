package com.galli.project.repository;

import java.util.List;
import java.util.Optional;

import com.galli.project.model.Circuit;

public interface CircuitRepository {

	public Circuit save(Circuit any);

	public void deleteById(long l);

	public List<Circuit> findAllByOrderByIdAsc();

	public Optional<Circuit> findById(long l);

}
