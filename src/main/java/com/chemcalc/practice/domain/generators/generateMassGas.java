package com.chemcalc.practice.domain.generators;

import java.math.BigDecimal;
import java.util.Random;

import com.chemcalc.practice.domain.Compound;

public class generateMassGas implements Constants{
	//Private constructor since this will only contain static methods
	//Instantiation by client code is forbidden
	private generateMassGas() {};
	
	static public String[] createMassGas(Random seededRandomNums, Compound compound) {
		int factorMol = 8;
		
		BigDecimal mol = new BigDecimal(seededRandomNums.nextDouble()*factorMol, fourDigit);
		BigDecimal mass = mol.multiply(compound.getMolarMass(), threeDigit);
		
		String question = String.format("Bereken het volume (in m<sup>3</sup>) van %s gram %s (g) bij 298 K",
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
		
		String[] questionContent = {question, answer};
		return questionContent;
	}
}
