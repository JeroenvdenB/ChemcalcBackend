package com.chemcalc.practice.controller;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.chemcalc.practice.domain.ProgramKey;

@Component
public interface ProgramKeyRepository extends CrudRepository<ProgramKey, Long> {
	@Query(value = "SELECT * FROM program_key WHERE key_string LIKE ?", nativeQuery = true)
	Optional<ProgramKey> findByKeyString(String key);
}
