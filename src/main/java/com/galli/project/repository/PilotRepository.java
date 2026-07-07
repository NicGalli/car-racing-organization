package com.galli.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.galli.project.model.Pilot;
import java.util.List;


public interface PilotRepository extends JpaRepository<Pilot, Long> {
	public List<Pilot> findByName(String name);
	public List<Pilot> findAllByOrderByIdAsc();
}
