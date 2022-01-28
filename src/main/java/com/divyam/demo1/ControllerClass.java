package com.divyam.demo1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ControllerClass {
	
	@GetMapping("/docker-run")
	public String getData() {
		return "Hey You are doing a great Job, Your application is running good";
	}

}
