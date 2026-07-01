package com.galli.project.model;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Pilot {

	@Id
	@GeneratedValue
	private Long id;
	private String name;

	public Pilot(String name) {
		this.name = name;
	}

	/**
	 * For testing only
	 * 
	 * @param id
	 * @param name
	 */
	public Pilot(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public Pilot() {}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Pilot [id=" + id + ", name=" + name + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pilot other = (Pilot) obj;
		return Objects.equals(id, other.id) && Objects.equals(name, other.name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

}
