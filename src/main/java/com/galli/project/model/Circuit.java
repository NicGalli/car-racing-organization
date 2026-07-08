package com.galli.project.model;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Circuit {

	@Id
	@GeneratedValue
	private Long id;
	private String name;
	private Long length;

	/**
	 * For testing only
	 * 
	 * @param id
	 * @param name
	 * @param length
	 */
	public Circuit(Long id, String name, Long length) {
		this.id = id;
		this.name = name;
		this.length = length;
	}

	@Override
	public String toString() {
		return "Circuit [id=" + id + ", name=" + name + ", length=" + length
				+ "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, length, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Circuit other = (Circuit) obj;
		return Objects.equals(id, other.id)
				&& Objects.equals(length, other.length)
				&& Objects.equals(name, other.name);
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

	public Long getLength() {
		return length;
	}

	public void setLength(Long length) {
		this.length = length;
	}

	/**
	 * @param name
	 * @param length
	 */
	public Circuit(String name, Long length) {
		this.name = name;
		this.length = length;
	}

	/**
	 * 
	 */
	public Circuit() {}

}
