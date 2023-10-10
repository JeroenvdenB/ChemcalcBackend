package com.chemcalc.practice.endpoints;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.chemcalc.practice.controller.CompoundService;
import com.chemcalc.practice.domain.Exercise;
import com.chemcalc.practice.domain.Compound;

@RestController
public class ExerciseEndpoint {
	@Autowired
	CompoundService compoundService;
	
	@GetMapping("generateExercise/{type}/{repetitions}/{seed}")
	public Exercise generateExercise(@PathVariable String type, @PathVariable int repetitions, @PathVariable long seed ) {
		
		//Use 'repetitions' from the pathvariable to call that exact number of random compounds.
		List<Compound> compounds = compoundService.randomCompound(repetitions);
		Exercise exercise = new Exercise(type, repetitions, seed);
		exercise.createQuestions(compounds);		
		return exercise;
	}
	
	@GetMapping("generateExercise/{type}/{repetitions}")
	public Exercise generateExercise(@PathVariable String type, @PathVariable int repetitions) {
		Exercise exercise = new Exercise(type, repetitions);
		List<Compound> compounds = compoundService.randomCompound(repetitions);
		exercise.createQuestions(compounds);
		return exercise;
	} 
	
	
}
