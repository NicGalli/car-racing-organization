package com.galli.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.galli.project.model.Pilot;

public interface PilotRepository extends JpaRepository<Pilot, Long> {}
