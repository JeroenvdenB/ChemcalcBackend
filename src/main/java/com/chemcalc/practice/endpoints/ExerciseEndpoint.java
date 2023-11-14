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
	
	@GetMapping("generateRandomExercise/{repetitions}/{seed}")
	public Exercise generateExercise(@PathVariable int repetitions, @PathVariable long seed ) {
		
		//Use 'repetitions' from the pathvariable to call that exact number of random compounds.
		List<Compound> compounds = compoundService.randomCompound(repetitions, seed);
		Exercise exercise = new Exercise("Random", repetitions, seed);
		exercise.createQuestions(compounds);		
		return exercise;
	}	
}
