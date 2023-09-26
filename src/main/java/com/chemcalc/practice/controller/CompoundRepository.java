package com.chemcalc.practice.controller;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.chemcalc.practice.domain.Compound;

@Component
public interface CompoundRepository extends CrudRepository<Compound, Long> {
	
}
