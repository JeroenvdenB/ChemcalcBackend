package com.chemcalc.practice.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.chemcalc.practice.domain.Compound;

@Component
public interface CompoundRepository extends CrudRepository<Compound, Long> {
	@Query(value = "SELECT * FROM compound ORDER BY RAND(?) LIMIT ?", nativeQuery = true)
	List<Compound> randomCompound(long seed, int limit);
}
