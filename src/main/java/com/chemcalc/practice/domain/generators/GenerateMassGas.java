package com.chemcalc.practice.domain.generators;

import java.math.BigDecimal;
import java.util.Random;

import com.chemcalc.practice.domain.Compound;
import com.chemcalc.practice.domain.Question;

public class GenerateMassGas implements Constants{
	private GenerateMassGas() {};
	
	static public Question create(long subseed, Compound compound) {
		int factorMol = 8;
		Random localRandomNums = new Random(subseed);
		
		BigDecimal mol = new BigDecimal(localRandomNums.nextDouble()*factorMol, fourDigit);
		BigDecimal mass = mol.multiply(compound.getMolarMass(), threeDigit);
		
		String text = String.format("Bereken het volume (in m<sup>3</sup>) van %s gram %s (g) bij 298 K",
				mass.toPlainString(), 
				compound.getHtmlFormula());
		
		// Step 1: Mass to mol: divide mass by molar mass
		// Step 2: Mol to volume gas: multiply mol by molar volume constant
		BigDecimal volumeGas = mol.multiply(molarVolume, threeDigit);
		
		String step1 = String.format("%s g / %s g/mol = %s mol", 
				mass.toPlainString(),
				compound.getMolarMass().round(fourDigit).toPlainString(),
				mol.toPlainString());
		
		String step2 = String.format("%s mol x V<sub>m</sub> = %s mol x %s m<sup>3</sup>/mol = %s m<sup>3</sup>", 
				mol.toPlainString(),
				mol.toPlainString(),
				molarVolume.toPlainString(),
				volumeGas.toPlainString());
		
		String answer = step1 + lineBreak + step2;
		
		return new Question(text, answer);
	}
}
