package com.galli.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PilotWebController {

	@GetMapping("/pilots")
	public String PilotsPage() {
		return "pilots-list";
	}
}
