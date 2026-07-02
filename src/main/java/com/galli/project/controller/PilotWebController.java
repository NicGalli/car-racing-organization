package com.galli.project.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.galli.project.model.Pilot;
import com.galli.project.model.PilotDTO;
import com.galli.project.service.PilotService;

@Controller
public class PilotWebController {

	private static final String MESSAGE = "message";
	private PilotService service;

	public PilotWebController(PilotService service) {
		this.service = service;
	}

	@GetMapping("/pilots")
	public String getPilotsPage(Model model) {
		List<Pilot> pilots = service.getAllPilots();
		model.addAttribute("pilots", pilots);
		model.addAttribute(MESSAGE, pilots.isEmpty() ? "No Pilots" : "");
		return "pilots-list";
	}

	@GetMapping("/pilots/edit/{id}")
	public String editPilotPage(Model model, @PathVariable Long id) {
		Pilot pilot = service.getPilotById(id);
		model.addAttribute("pilot", pilot);
		model.addAttribute(MESSAGE,
				pilot == null ? "No Pilot found with id " + id : "");
		return "edit-pilot";
	}

	@GetMapping("/pilots/new")
	public String addPilotPage(Model model) {
		model.addAttribute("pilot", new Pilot());
		model.addAttribute(MESSAGE, "");
		return "edit-pilot";
	}

	@PostMapping("/pilots/save")
	public String savePilot(PilotDTO pilotDTO) {

		Pilot pilot = new Pilot();
		pilot.setId(pilotDTO.getId());
		pilot.setName(pilotDTO.getName());

		Long id = pilot.getId();

		if (id == null) {
			service.insertNewPilot(pilot);
		} else {
			service.updatePilotById(id, pilot);
		}
		return "redirect:/pilots";
	}

	@PostMapping("/pilots/delete/{id}")
	public String deletePilot(@PathVariable Long id) {
		service.deletePilotById(id);
		return "redirect:/pilots";
	}
}
