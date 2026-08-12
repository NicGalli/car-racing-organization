package com.galli.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.galli.project.model.Race;

public interface RaceRepository extends JpaRepository<Race, Long> {
	public List<Race> findByName(String name);
	public List<Race> findAllByOrderByIdAsc();
}
