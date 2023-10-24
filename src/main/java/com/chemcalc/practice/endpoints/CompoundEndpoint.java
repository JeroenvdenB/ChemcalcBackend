package com.chemcalc.practice.endpoints;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.chemcalc.practice.domain.Compound;
import com.chemcalc.practice.domain.ProgramKey;
import com.chemcalc.practice.controller.CompoundService;
import com.chemcalc.practice.controller.ProgramKeyService;

@RestController
public class CompoundEndpoint {
	@Autowired
	CompoundService compoundService;
	
	@Autowired
	ProgramKeyService programKeyService;
	
	@GetMapping("allCompounds")
	public Iterable<Compound> allCompounds() {
		return compoundService.getAllCompounds();
	}
	
	@GetMapping("compoundById/{id}")
	public Optional<Compound> compoundById(@PathVariable("id") long compoundId) {
		return compoundService.findById(compoundId);
	}
	
	@PostMapping("addCompound")
	public void addCompound(@RequestHeader("PRODUCT_KEY") String key, @RequestBody Compound compound) {
		//Standard HTTP error codes suffice here. 
		//Throwing a ResponseStatusException will get the code across
		//The front-end checks for this code, handling the exception if it comes up for clear user feedback.
		//This is an alternative to sending a whole ResponseEntity as a return object.
		Optional<ProgramKey> keyOptional = programKeyService.findByKeyString(key);
		if (keyOptional.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No valid product key found");
		} else {
			compoundService.saveCompound(compound);			
		}
	}
	
	@DeleteMapping("deleteCompound")
	public void deleteCompound(@RequestHeader("PRODUCT_KEY") String key, @RequestBody String recievedId) {
		Optional<ProgramKey> keyOptional = programKeyService.findByKeyString(key);
		if (keyOptional.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No valid product key found");
		} else {
			long id = Long.parseLong(recievedId);
			compoundService.deleteCompound(id);						
		}
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
	public void updateCompound(@RequestHeader("PRODUCT_KEY") String key, @RequestBody Compound compound) {
		Optional<ProgramKey> keyOptional = programKeyService.findByKeyString(key);
		if (keyOptional.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No valid product key found");
		} 
		
		//Note: no id supplied in url. Make sure it's in the compound object on the client-side.
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
