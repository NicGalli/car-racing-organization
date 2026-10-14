package com.galli.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomepageWebController {

	@GetMapping("/homepage")
	public String getHomepage() {
		return "homepage";
	}
}
