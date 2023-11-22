package com.chemcalc.practice.endpoints;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.chemcalc.practice.controller.CompoundService;
import com.chemcalc.practice.domain.Exercise;
import com.chemcalc.practice.domain.Compound;

@RestController
public class ExerciseEndpoint {
	@Autowired
	CompoundService compoundService;
	
	@GetMapping("anyCompoundExercise/{type}/{repetitions}/{seed}")
	public Exercise generateAnyCompound(@PathVariable String type, @PathVariable int repetitions,
			@PathVariable long seed) {
		// Check that the provided type is allowed for the compound choice
		// Valid for any compound would be:
		String[] allowedQuestionTypes = {"Random", "MolMass", "MassMol", "MolParticles", "MassParticles"};
		
		if (Arrays.asList(allowedQuestionTypes).contains(type)) {
			List<Compound> compounds = compoundService.randomCompound(repetitions, seed);
			Exercise exercise = new Exercise(type, repetitions, seed);
			exercise.fillQuestionsList(compounds);		
			return exercise;
		} else {
			String error = String.format("Invalid question type. %s is not allowed for %s.",
					type, "any compound");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, error);
		}
	}
	
	//TODO: build non-metal non-water endpoint
	//TODO: build molecular non-water endpoint
	//TODO: build non-salt endpoint
	//TODO: build salt-only endpoint
	//TODO: build gas-only endpoint
	//TODO: build density-exists endpoint
	
	
	
}
