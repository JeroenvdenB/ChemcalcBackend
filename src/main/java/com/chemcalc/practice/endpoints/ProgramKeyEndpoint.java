package com.chemcalc.practice.endpoints;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.chemcalc.practice.controller.ProgramKeyService;
import com.chemcalc.practice.domain.ProgramKey;

@RestController
public class ProgramKeyEndpoint {
	@Autowired
	ProgramKeyService programKeyService;
	
	//For debugging - remove later
	@GetMapping("echo/{key}")
	public String hashed(@PathVariable("key") String key) {
		return key;
	}
	
	@GetMapping("verifyKey/{key}")
	public ProgramKey verifyKey(@PathVariable("key") String key) {	
		Optional<ProgramKey> keyOptional = programKeyService.findByKeyString(key);
		if (keyOptional.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product key not found in database");
		} else {
			return keyOptional.get();
		}
	}
	
}
