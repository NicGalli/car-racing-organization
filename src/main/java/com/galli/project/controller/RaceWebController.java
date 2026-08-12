package com.galli.project.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.galli.project.model.Race;
import com.galli.project.service.RaceService;

@Controller
public class RaceWebController {

	private static final String MESSAGE = "message";
	private RaceService service;

	public RaceWebController(RaceService service) {
		this.service = service;
	}

	@GetMapping("/races")
	public String getRacesPage(Model model) {
		List<Race> races = service.getAllRaces();
		model.addAttribute("races", races);
		model.addAttribute(MESSAGE, races.isEmpty() ? "No Races" : "");
		return "races-list";
	}
}
