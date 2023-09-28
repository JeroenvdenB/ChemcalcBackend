package com.chemcalc.practice.endpoints;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.chemcalc.practice.domain.Compound;
import com.chemcalc.practice.controller.CompoundService;

@RestController
public class CompoundEndpoint {
	@Autowired
	CompoundService compoundService;
	
	@GetMapping("allCompounds")
	public Iterable<Compound> allCompounds() {
		return compoundService.getAllCompounds();
	}
	
	@GetMapping("compoundById/{id}")
	public Optional<Compound> compoundById(@PathVariable("id") long compoundId) {
		return compoundService.findById(compoundId);
	}
	
	@PostMapping("addCompound")
	public void addCompound(@RequestBody Compound compound) {
		compoundService.saveCompound(compound);
	}
	
	@DeleteMapping("deleteCompound")
	public void deleteCompound(@RequestBody String recievedId) {
		long id = Long.parseLong(recievedId);
		compoundService.deleteCompound(id);
	}
	
	//UpdateCompound is the only untested endpoint rn
	@PutMapping("updateCompound")
	public void updateCompound(@RequestBody Compound compound) {
		//Note: no id supplied in url. Make sure it's in the compound object!
		Optional<Compound> compoundOptional = compoundService.findById(compound.getId());
		
		if (compoundOptional.isEmpty()) {
			System.out.println("Error in updating compound. No compound found by id.");
			return;
		} else {		
			Compound dbCompound = compoundOptional.get();
			dbCompound.setMolarMass(compound.getMolarMass());
			dbCompound.setDensity(compound.getDensity());
			dbCompound.setName(compound.getName());
			dbCompound.setHtmlFormula(compound.getHtmlFormula());
			dbCompound.setComposition(compound.getComposition());
			dbCompound.setPhase(compound.getPhase());
			
			compoundService.saveCompound(dbCompound);		
			System.out.println("All properties updated through endpoint updateCompound.");
		}
	}
}