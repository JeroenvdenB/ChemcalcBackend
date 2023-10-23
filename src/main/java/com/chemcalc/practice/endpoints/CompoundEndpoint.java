package com.chemcalc.practice.endpoints;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
	
	@PutMapping("madeUpHeader")
	public void header(@RequestHeader("made-up") String thing) { // This works, just make sure to add the header to the CORS filter
		System.out.println(thing);
		//include API key in headers for DB-altering endpoints
		//And check if the provided key is in the database
		//The 'login' is also just a key-check to let a user onto the page
	}
	
	@DeleteMapping("deleteCompound")
	public void deleteCompound(@RequestBody String recievedId) {
		long id = Long.parseLong(recievedId);
		compoundService.deleteCompound(id);
	}
	
	@GetMapping("randomCompound/{limit}")
	public List<Compound> randomCompound(@PathVariable("limit") Integer limit) {
		Random random = new Random();
		int seed = random.nextInt(); 
		//seed is automatically promoted to long for randomCompound(int, long)
		return compoundService.randomCompound(limit, seed); 
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
			dbCompound.setType(compound.getType());
			
			compoundService.saveCompound(dbCompound);		
		}
	}
}
