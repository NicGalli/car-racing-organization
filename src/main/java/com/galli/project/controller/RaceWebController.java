package com.galli.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RaceWebController {

	@GetMapping("/races")
	public String getPilotsPage() {
		return "races-list";
	}
}
