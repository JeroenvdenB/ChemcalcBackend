package com.chemcalc.practice.domain;

import java.math.BigDecimal;
import java.math.MathContext;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.SequenceGenerator;

@Entity
public class Compound {
	
	@SequenceGenerator(name="defSequence", allocationSize=1)
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, 
			generator = "defSequence")
	private long id;
	
	@Column(name="molar_mass", precision=38, scale=3)
	private BigDecimal molarMass;
	
	@Column(name="density", precision=38, scale=3)
	private BigDecimal density;
	
	private String name;
	private String htmlFormula;
	private String composition;
	private String phase;
	private String type;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHtmlFormula() {
		return htmlFormula;
	}
	public void setHtmlFormula(String htmlFormula) {
		this.htmlFormula = htmlFormula;
	}
	public String getComposition() {
		return composition;
	}
	public void setComposition(String composition) {
		this.composition = composition;
	}
	public BigDecimal getDensity() {
		return density;
	}
	public void setDensity(BigDecimal density) {
		this.density = density;
	}
	public String getPhase() {
		return phase;
	}
	public void setPhase(String phase) {
		this.phase = phase;
	}
	public BigDecimal getMolarMass() {
		return molarMass;
	}
	public void setMolarMass(BigDecimal molarMass) {
		this.molarMass = molarMass;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

}
