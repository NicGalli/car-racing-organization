package com.galli.project.model;

public class CircuitDTO {
	
	private Long id;
	private String name;
	private Long length;

	/**
	 * @param id
	 * @param name
	 * @param length
	 */
	public CircuitDTO(Long id, String name, Long length) {
		this.id = id;
		this.name = name;
		this.length = length;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Long getLength() {
		return length;
	}
}
