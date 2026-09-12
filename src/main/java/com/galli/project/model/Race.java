package com.galli.project.model;

import static org.hibernate.annotations.OnDeleteAction.SET_NULL;

import java.util.Objects;
import java.util.Set;

import org.hibernate.annotations.OnDelete;

import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

@Entity
public class Race {

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	@ManyToOne
	@OnDelete(action = SET_NULL)
	private Circuit circuit;

	@ManyToMany
	@JoinTable(
			inverseJoinColumns = @JoinColumn(
				name = "pilot_id",
				foreignKey = @ForeignKey(
					foreignKeyDefinition = "FOREIGN KEY (pilot_id) REFERENCES pilot(id) ON DELETE CASCADE"
				)
			)
		)
	private Set<Pilot> pilots;

	/**
	 * 
	 */
	public Race() {}

	/**
	 * @param name
	 * @param circuit
	 * @param pilots
	 */
	public Race(String name, Circuit circuit, Set<Pilot> pilots) {
		this.name = name;
		this.circuit = circuit;
		this.pilots = pilots;
	}

	/**
	 * For testing only
	 * 
	 * @param id
	 * @param name
	 * @param circuit
	 * @param pilots
	 */
	public Race(Long id, String name, Circuit circuit, Set<Pilot> pilots) {
		this.id = id;
		this.name = name;
		this.circuit = circuit;
		this.pilots = pilots;
	}

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

	public Set<Pilot> getPilots() {
		return pilots;
	}

	public void setPilots(Set<Pilot> pilots) {
		this.pilots = pilots;
	}

	@Override
	public int hashCode() {
		return Objects.hash(circuit, id, name, pilots);
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
				&& Objects.equals(pilots, other.pilots);
	}

	@Override
	public String toString() {
		return "Race [id=" + id + ", name=" + name + ", circuit=" + circuit
				+ ", pilots=" + pilots + "]";
	}

}
