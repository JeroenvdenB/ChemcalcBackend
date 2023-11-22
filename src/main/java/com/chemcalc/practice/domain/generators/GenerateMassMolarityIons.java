package com.chemcalc.practice.domain.generators;

import java.math.BigDecimal;
import java.util.Random;

import com.chemcalc.practice.domain.Compound;
import com.chemcalc.practice.domain.Question;

public class GenerateMassMolarityIons implements Constants{
	private GenerateMassMolarityIons() {};
	
	static public Question create(long subseed, Compound compound) {
		Random localRandomNums = new Random(subseed);
		int factorVolume =16;
		double factorMass = 400;
		
		BigDecimal volume = new BigDecimal(localRandomNums.nextDouble()*factorVolume, threeDigit);
		BigDecimal mass = new BigDecimal(localRandomNums.nextDouble()*factorMass, threeDigit);
		
		// Certain tiny volumes create nonsense answers.
		// This is a simple safe-guard slapped in place to prevent the occasional answer like 100+ M
		if (volume.doubleValue() < 0.1)
			volume = volume.add(new BigDecimal(1), threeDigit);
		
		// Grab composition code in the form "X:3, Y:1, Z:1", split, and randomly select an ion
		String composition = compound.getComposition();
		String[] ions = composition.split(",");
		int ionIndex = (int) Math.floor(localRandomNums.nextDouble(0.99d)*ions.length);
		String[] atom = ions[ionIndex].trim().split(":");
		String symbol = atom[0];
		BigDecimal coefficient = new BigDecimal(atom[1]);
		
		String text = String.format("Bereken [%s] als %s g %s word opgelost in %s L oplosmiddel", 
				symbol,
				mass.toPlainString(),
				compound.getHtmlFormula(),
				volume.toPlainString());
		
		// Step 1: mass to mol: divide mass by molar mass
		// Step 2: mol salt to mol ions: multiply mol salt by coefficient
		// Step 3: mol ions to molarity: divide mol ions by volume
		BigDecimal molSalt = mass.divide(compound.getMolarMass(), fourDigit) ;
		BigDecimal molIons = molSalt.multiply(coefficient, fourDigit);
		BigDecimal molarity = molIons.divide(volume, threeDigit);
		String step1 = String.format("%s g / %s g/mol = %s mol %s", 
				mass.toPlainString(),
				compound.getMolarMass().round(fourDigit).toPlainString(),
				molSalt.toPlainString(),
				compound.getHtmlFormula());
		String step2 = String.format("%s mol x %s = %s mol %s", 
				molSalt.toPlainString(),
				coefficient.toPlainString(),
				molIons.toPlainString(),
				symbol);
		String step3 = String.format("[%s] = %s mol / %s L = %s M", 
				symbol,
				molIons.toPlainString(),
				volume.toPlainString(),
				molarity.toPlainString());
		String answer = step1 + lineBreak + step2 + lineBreak + step3;
		
		return new Question(text, answer);
	}
}
