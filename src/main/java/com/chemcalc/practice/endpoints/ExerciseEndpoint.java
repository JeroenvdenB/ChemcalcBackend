package com.chemcalc.practice.endpoints;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.chemcalc.practice.domain.Exercise;

@RestController
public class ExerciseEndpoint {
	
	@GetMapping("generateExercise/{type}/{repetitions}/{seed}")
	public Exercise generateExercise(@PathVariable String type, @PathVariable int repetitions, @PathVariable long seed ) {
		Exercise exercise = new Exercise(type, repetitions, seed);
		return exercise;
	}
	
	@GetMapping("generateExercise/{type}/{repetitions}")
	public Exercise generateExercise(@PathVariable String type, @PathVariable int repetitions) {
		Exercise exercise = new Exercise(type, repetitions);
		return exercise;
	} 
	
	
}
