package com.chemcalc.practice.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chemcalc.practice.domain.ProgramKey;

@Service
public class ProgramKeyService {
	@Autowired
	ProgramKeyRepository programKeyRepository;
	
	public Optional<ProgramKey> findByKeyString(String key) {
		Optional<ProgramKey> keyOptional = programKeyRepository.findByKeyString(key);
		return keyOptional;
	}
}
