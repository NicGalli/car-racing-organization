package com.galli.project.model;

import java.util.List;

public class RaceDTO {

	private Long id;

	private String name;

	private Circuit circuit;

	private List<Pilot> pilotsList;

	public RaceDTO(Long id, String name, Circuit circuit,
			List<Pilot> pilotsList) {
		this.id = id;
		this.name = name;
		this.circuit = circuit;
		this.pilotsList = pilotsList;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Circuit getCircuit() {
		return circuit;
	}

	public List<Pilot> getPilotsList() {
		return pilotsList;
	}

}
