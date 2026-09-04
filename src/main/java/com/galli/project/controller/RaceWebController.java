package com.galli.project.controller;

import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.galli.project.model.Circuit;
import com.galli.project.model.Pilot;
import com.galli.project.model.Race;
import com.galli.project.model.RaceDTO;
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
	public String viewRacePage(Model model, @PathVariable Long id) {
		Race race = service.getRaceById(id);
		List<Circuit> allCircuits = service.getAllCircuits();
		List<Pilot> allOtherPilots = service.getAllOtherPilots(id);

		model.addAttribute("race", race);
		model.addAttribute("allCircuits", allCircuits);
		model.addAttribute("allOtherPilots", allOtherPilots);
		model.addAttribute(MESSAGE,
				race == null ? "No Race found with id " + id : "");
		return "view-race";
	}

	@GetMapping("/races/new")
	public String addRacePage(Model model) {
		model.addAttribute("race", new Race());
		model.addAttribute("allCircuits", service.getAllCircuits());
		model.addAttribute(MESSAGE, "");
		return "view-race";
	}

	@PostMapping("/races/save")
	public String saveRace(RaceDTO raceDTO) {
		Race race = new Race();
		race.setId(raceDTO.getId());
		race.setName(raceDTO.getName());
		race.setCircuit(raceDTO.getCircuit());
		List<Pilot> pilots = raceDTO.getPilotsList();
		race.setPilotsList(pilots == null ? new HashSet<>()
				: new HashSet<>(pilots));

		Long id = race.getId();

		if (id == null) {
			id = service.insertNewRace(race).getId();
		} else {
			id = service.updateRaceById(id, race).getId();
		}

		return "redirect:/races/view/" + id;
	}

	@PostMapping("/races/delete/{id}")
	public String deleteRace(@PathVariable Long id) {
		service.deleteRaceById(id);
		return "redirect:/races";
	}
}
