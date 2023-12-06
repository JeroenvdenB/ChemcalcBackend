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

	@GetMapping("getExercise/{type}/{repetitions}/{seed}")
	public Exercise generateExercise(@PathVariable String type, @PathVariable int repetitions,
			@PathVariable long seed) {
		
		// A guide to show what type requires what compound type query
		String[] needsRandomCompounds = {"Random", "MolMass", "MassMol", "MolParticles", "MassParticles"};
		String[] needsNonMetalNonWater = {"MolMolarity", "MolarityMol", "MolarityVolume"};
		String[] needsMolecularNonWater = {"MolarityMass", "MassMolarity"};
		String[] needsNonSalt = {"ParticlesMol", "ParticlesMass"};
		String[] needsSalt = {"MolMolarityIons", "MolarityIonsMol", "MolarityIonsMass", "MassMolarityIons"};
		String[] needsGas = {"MolGas", "GasMol", "MassGas", "GasMass"};
		String[] needsDensity = {"MassVolume", "VolumeMass"};

		// Determine what service to use (aka what compounds to request) based on the question type
		// Get compounds from database.
		List<Compound> compounds;
		if (Arrays.asList(needsRandomCompounds).contains(type)) {
			compounds = compoundService.randomCompound(repetitions, seed);
		} else if (Arrays.asList(needsNonMetalNonWater).contains(type)) {
			compounds = compoundService.nonMetalNonWater(repetitions, seed);
		} else if (Arrays.asList(needsMolecularNonWater).contains(type)) {
			compounds = compoundService.molecularNonWater(repetitions, seed);
		} else if (Arrays.asList(needsNonSalt).contains(type)) {
			compounds = compoundService.nonSalt(repetitions, seed);
		} else if (Arrays.asList(needsSalt).contains(type)) {
			compounds = compoundService.saltOnly(repetitions, seed);
		} else if (Arrays.asList(needsGas).contains(type)) {
			compounds = compoundService.gasOnly(repetitions, seed);
		} else if (Arrays.asList(needsDensity).contains(type)) {
			compounds = compoundService.density(repetitions, seed);
		}else {
			String error = String.format("Invalid question type. %s is not allowed for %s.",
					type, "any compound");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, error);
		}
		
		Exercise exercise = new Exercise(type, repetitions, seed);
		exercise.fillQuestionsList(compounds);	
		
		return exercise;
	}	
}
