package com.galli.project.model;

import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

@Entity
public class Race {

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	@ManyToOne
	private Circuit circuit;
	
	@ManyToMany
	private Set<Pilot> pilotsList;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Circuit getCircuit() {
		return circuit;
	}

	public void setCircuit(Circuit circuit) {
		this.circuit = circuit;
	}

	public Set<Pilot> getPilotsList() {
		return pilotsList;
	}

	public void setPilotsList(Set<Pilot> pilotsList) {
		this.pilotsList = pilotsList;
	}

	@Override
	public int hashCode() {
		return Objects.hash(circuit, id, name, pilotsList);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Race other = (Race) obj;
		return Objects.equals(circuit, other.circuit)
				&& Objects.equals(id, other.id)
				&& Objects.equals(name, other.name)
				&& Objects.equals(pilotsList, other.pilotsList);
	}

	@Override
	public String toString() {
		return "Race [id=" + id + ", name=" + name + ", circuit=" + circuit
				+ ", pilotsList=" + pilotsList + "]";
	}

	/**
	 * 
	 */
	public Race() {}

	/**
	 * @param name
	 * @param circuit
	 * @param pilotsList
	 */
	public Race(String name, Circuit circuit, Set<Pilot> pilotsList) {
		this.name = name;
		this.circuit = circuit;
		this.pilotsList = pilotsList;
	}

	/**
	 * For testing only
	 * 
	 * @param id
	 * @param name
	 * @param circuit
	 * @param pilotsList
	 */
	public Race(Long id, String name, Circuit circuit, Set<Pilot> pilotsList) {
		this.id = id;
		this.name = name;
		this.circuit = circuit;
		this.pilotsList = pilotsList;
	}

}
