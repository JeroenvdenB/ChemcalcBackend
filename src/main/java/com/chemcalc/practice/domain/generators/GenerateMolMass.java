package com.chemcalc.practice.domain.generators;

import java.math.BigDecimal;
import java.util.Random;

import com.chemcalc.practice.domain.Compound;
import com.chemcalc.practice.domain.Question;

public class GenerateMolMass implements Constants{
	private GenerateMolMass() {};
	
	static public Question create(long subseed, Compound compound) {
		Random localRandomNums = new Random(subseed);
		int factor = 3;

		BigDecimal mol = new BigDecimal(localRandomNums.nextDouble()*factor, threeDigit);
		String text = String.format("Bereken hoeveel gram overeenkomt met %s mol %s", 
				mol.toPlainString(), 
				compound.getHtmlFormula());
		
		// moles to mass: multiply moles by molar mass
		BigDecimal mass = mol.multiply(compound.getMolarMass(), threeDigit);
		String answer = String.format("%s mol x %s g/mol = %s gram %s", 
				mol.toPlainString(), 
				compound.getMolarMass().round(fourDigit).toPlainString(), 
				mass.toPlainString(), 
				compound.getHtmlFormula());
			
		return new Question(text, answer);
	}
}
