package com.galli.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.galli.project.model.Pilot;
import com.galli.project.service.PilotService;

@Controller
public class PilotWebController {

	@Autowired
	private PilotService pilotService;

	@GetMapping("/pilots")
	public String PilotsPage(Model model) {
		List<Pilot> pilots = pilotService.getAllPilots();
		model.addAttribute("pilots", pilots);
		model.addAttribute("message", pilots.isEmpty() ? "No Pilots" : "");
		return "pilots-list";
	}

	@GetMapping("/pilots/edit/{id}")
	public String PilotsPage(Model model, @PathVariable Long id) {
		Pilot pilot = pilotService.getPilotById(id);
		model.addAttribute("pilot", pilot);
		model.addAttribute("message",
				pilot == null ? "No Pilot found with id " + id : "");
		return "edit-pilot";
	}
}
