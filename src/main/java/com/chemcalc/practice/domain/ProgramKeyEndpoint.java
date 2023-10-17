package com.chemcalc.practice.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chemcalc.practice.controller.ProgramKeyService;

@RestController
public class ProgramKeyEndpoint {
	@Autowired
	ProgramKeyService programKeyService;
	
	@PostMapping("verifyKey/{key}")
	public void verifyKey(@PathVariable("key") String key) {
		System.out.println(key);
	}
}
