package com.galli.project.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

	@GetMapping("/races/view/{id}")
	public String editRacePage(Model model, @PathVariable Long id) {
		Race race = service.getRaceById(id);
		model.addAttribute("race", race);
		model.addAttribute(MESSAGE,
				race == null ? "No Race found with id " + id : "");
		return "view-race";
	}
}
