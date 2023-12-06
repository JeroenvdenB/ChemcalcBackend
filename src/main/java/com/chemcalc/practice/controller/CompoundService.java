package com.chemcalc.practice.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.chemcalc.practice.domain.Compound;

@Service
public class CompoundService {
	@Autowired
	CompoundRepository compoundRepository;
	
	public void saveCompound(Compound compound) {
		compoundRepository.save(compound);
	}
	
	public Iterable<Compound> getAllCompounds() {
		return compoundRepository.findAll();
	}
	
	public Optional<Compound> findById(long id) {
		return compoundRepository.findById(id);
	}
	
	public void deleteCompound(long id) {
		compoundRepository.deleteById(id);
	}
	
	public List<Compound> randomCompound(int limit, long seed) {
		return compoundRepository.randomCompound(seed, limit);
	}	
	
	public List<Compound> nonMetalNonWater(int limit, long seed) {
		return compoundRepository.notMetalNotWater(seed, limit);
	}
	
	public List<Compound> molecularNonWater(int limit, long seed) {
		return compoundRepository.molecularNonWater(seed, limit);
	}
	
	public List<Compound> nonSalt(int limit, long seed) {
		return compoundRepository.nonSalt(seed, limit);
	}
	
	public List<Compound> saltOnly(int limit, long seed) {
		return compoundRepository.saltOnly(seed, limit);
	}
	
	public List<Compound> gasOnly(int limit, long seed) {
		return compoundRepository.gasOnly(seed, limit);
	}
	
	public List<Compound> density(int limit, long seed) {
		return compoundRepository.density(seed, limit);
	}
}
