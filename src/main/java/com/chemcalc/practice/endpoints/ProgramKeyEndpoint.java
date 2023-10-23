package com.chemcalc.practice.endpoints;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chemcalc.practice.controller.ProgramKeyService;
import com.chemcalc.practice.domain.ProgramKey;

@RestController
public class ProgramKeyEndpoint {
	@Autowired
	ProgramKeyService programKeyService;
	
	@GetMapping("echoKey/{key}")
	public String hashed(@PathVariable("key") String key) {
		return key;
	}
	
	@GetMapping("verifyKey/{key}")
	public ProgramKey verifyKey(@PathVariable("key") String key) {
		//Returns an empty ProgramKey object if the key does not exist in the database.
		
		Optional<ProgramKey> keyOptional = programKeyService.findByKeyString(key);
		if (keyOptional.isEmpty()) {
			ProgramKey pk = new ProgramKey();
			return pk;
		} else {
			return keyOptional.get();
		}
	}
	
}
