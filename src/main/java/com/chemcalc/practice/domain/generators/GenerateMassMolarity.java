package com.chemcalc.practice.domain.generators;

import java.math.BigDecimal;
import java.util.Random;

import com.chemcalc.practice.domain.Compound;
import com.chemcalc.practice.domain.Question;

public class GenerateMassMolarity implements Constants {
	private GenerateMassMolarity() {};
	
	static public Question create(long subseed, Compound compound) {
		Random localRandomNums = new Random(subseed);
		int factorVolume =16;
		double factorMass = 500;
		
		BigDecimal volume = new BigDecimal(localRandomNums.nextDouble()*factorVolume, threeDigit);
		BigDecimal mass = new BigDecimal(localRandomNums.nextDouble()*factorMass, threeDigit);
		
		// Certain tiny volumes create nonsense answers.
		// This is a simple safe-guard slapped in place to prevent answers like 300 M...
		if (volume.doubleValue() < 0.1)
			volume = volume.add(new BigDecimal(1), threeDigit);
		
		String text = String.format("Bereken de %s van %s g %s in %s L oplosmiddel", 
				(localRandomNums.nextDouble() < 0.4) ? "concentratie (in mol/L)" : "molariteit", 
				mass.toPlainString(),
				compound.getHtmlFormula(),
				volume.toPlainString());
		
		// Step 1: mass to mol: divide mass by molar mass
		// Step 2: mol to molarity: divide mol by volume
		BigDecimal mol = mass.divide(compound.getMolarMass(), fourDigit);
		BigDecimal molarity = mol.divide(volume, threeDigit);
		String step1 = String.format("%s g / %s g/mol = %s mol", 
				mass.toPlainString(),
				compound.getMolarMass().round(fourDigit).toPlainString(),
				mol.toPlainString());
		String step2 = String.format("%s mol / %s L = %s M",
				mol.toPlainString(),
				volume.toPlainString(),
				molarity.toPlainString());
		String answer = step1 + lineBreak + step2;
		
		return new Question(text, answer);
	}
}
