package com.galli.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.galli.project.model.Circuit;

public interface CircuitRepository extends JpaRepository<Circuit, Long> {
	public List<Circuit> findAllByOrderByIdAsc();

}
