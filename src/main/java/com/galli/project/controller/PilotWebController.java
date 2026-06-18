package com.galli.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
}
