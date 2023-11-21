package com.chemcalc.practice.domain.generators;

import java.math.BigDecimal;
import java.util.Random;

import com.chemcalc.practice.domain.Compound;
import com.chemcalc.practice.domain.Question;

public class GenerateMassMol implements Constants{
	private GenerateMassMol() {};
	
	static public Question create(long subseed, Compound compound) {
		Random localRandomNums = new Random(subseed);
		int factorMass = 80;
			
		BigDecimal mass = new BigDecimal(localRandomNums.nextDouble()*factorMass, threeDigit);
		String text = String.format("Bereken hoeveel mol overeenkomt met %s gram %s", 
				mass.toPlainString(), 
				compound.getHtmlFormula());
	
			
		// mass to moles: divide the mass by the molar mass
		BigDecimal mol = mass.divide(compound.getMolarMass(), threeDigit);
		String answer = String.format("%s g / %s g/mol = %s mol %s", 
				mass.toPlainString(), 
				compound.getMolarMass().round(fourDigit).toPlainString(), 
				mol.toPlainString(), 
				compound.getHtmlFormula());	
		
		return new Question(text, answer);
	}
}
