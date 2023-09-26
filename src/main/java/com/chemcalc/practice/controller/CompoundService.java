package com.chemcalc.practice.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
}
