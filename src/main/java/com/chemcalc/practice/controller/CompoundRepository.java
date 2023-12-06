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
	
	@Query(value = "SELECT * FROM compound WHERE type <> 'metaal' AND name <> 'water' ORDER BY RAND(?) LIMIT ?", nativeQuery = true)
	List<Compound> notMetalNotWater(long seed, int limit);
	
	@Query(value = "SELECT * FROM compound WHERE type = 'moleculair' AND name <> 'water' ORDER BY RAND(?) LIMIT ?", nativeQuery = true)
	List<Compound> molecularNonWater(long seed, int limit);
	
	@Query(value = "SELECT * FROM compound WHERE type <> 'zout' ORDER BY RAND(?) LIMIT ?", nativeQuery = true)
	List<Compound> nonSalt(long seed, int limit);
	
	@Query(value = "SELECT * FROM compound WHERE type = 'zout' ORDER BY RAND(?) LIMIT ?", nativeQuery = true)
	List<Compound> saltOnly(long seed, int limit);
	
	@Query(value = "SELECT * FROM compound WHERE phase = 'g' ORDER BY RAND(?) LIMIT ?", nativeQuery = true)
	List<Compound> gasOnly(long seed, int limit);
	
	@Query(value = "SELECT * FROM compound WHERE density <> 0 ORDER BY RAND(?) LIMIT ?", nativeQuery = true)
	List<Compound> density(long seed, int limit);
}
