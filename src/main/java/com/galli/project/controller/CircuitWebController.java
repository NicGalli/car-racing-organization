package com.galli.project.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.galli.project.model.Circuit;
import com.galli.project.model.CircuitDTO;
import com.galli.project.service.CircuitService;

@Controller
public class CircuitWebController {

	private static final String MESSAGE = "message";
	private CircuitService service;

	public CircuitWebController(CircuitService service) {
		this.service = service;
	}

	@GetMapping("/circuits")
	public String getCircuitsPage(Model model) {
		List<Circuit> circuits = service.getAllCircuits();
		model.addAttribute("circuits", circuits);
		model.addAttribute(MESSAGE, circuits.isEmpty() ? "No Circuits" : "");
		return "circuits-list";
	}

	@GetMapping("/circuits/edit/{id}")
	public String editCircuitPage(Model model, @PathVariable Long id) {
		Circuit circuit = service.getCircuitById(id);
		model.addAttribute("circuit", circuit);
		model.addAttribute(MESSAGE,
				circuit == null ? "No Circuit found with id " + id : "");
		return "edit-circuit";
	}

	@GetMapping("/circuits/new")
	public String addCircuitPage(Model model) {
		model.addAttribute("circuit", new Circuit());
		model.addAttribute(MESSAGE, "");
		return "edit-circuit";
	}

	@PostMapping("/circuits/save")
	public String saveCircuit(CircuitDTO circuitDTO) {

		Circuit circuit = new Circuit();
		circuit.setId(circuitDTO.getId());
		circuit.setName(circuitDTO.getName());
		circuit.setLength(circuitDTO.getLength());

		Long id = circuit.getId();

		if (id == null) {
			service.insertNewCircuit(circuit);
		} else {
			service.updateCircuitById(id, circuit);
		}
		return "redirect:/circuits";
	}

	@PostMapping("/circuits/delete/{id}")
	public String deleteCircuit(@PathVariable Long id) {
		service.deleteCircuitById(id);
		return "redirect:/circuits";
	}
}
