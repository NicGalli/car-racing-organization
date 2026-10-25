package com.galli.project.model;

import java.util.List;

public class RaceDTO {

	private final Long id;

	private final String name;

	private final Circuit circuit;

	private final List<Pilot> pilots;

	public RaceDTO(Long id, String name, Circuit circuit,
			List<Pilot> pilots) {
		this.id = id;
		this.name = name;
		this.circuit = circuit;
		this.pilots = pilots;
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

	public List<Pilot> getPilots() {
		return pilots;
	}

}
