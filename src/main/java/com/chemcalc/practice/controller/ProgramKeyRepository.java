package com.chemcalc.practice.controller;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.chemcalc.practice.domain.ProgramKey;

@Component
public interface ProgramKeyRepository extends CrudRepository<ProgramKey, Long> {
	
}
