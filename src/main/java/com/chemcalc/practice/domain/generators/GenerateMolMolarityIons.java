package com.chemcalc.practice.domain.generators;

import java.math.BigDecimal;
import java.util.Random;

import com.chemcalc.practice.domain.Compound;
import com.chemcalc.practice.domain.Question;

public class GenerateMolMolarityIons implements Constants{
	private GenerateMolMolarityIons() {};
	
	static public Question create(long subseed, Compound compound) {
		Random localRandomNums = new Random(subseed);
		int factorMole = 2;
		int factorVolume = 10;
		
		BigDecimal volume = new BigDecimal(localRandomNums.nextDouble()*factorVolume, threeDigit);
		BigDecimal mol = new BigDecimal(localRandomNums.nextDouble()*factorMole, threeDigit);
		
		//Grab composition code in the form "X:3, Y:1, Z:1", split, and randomly select an ion
		String composition = compound.getComposition();
		String[] ions = composition.split(",");
		int ionIndex = (int) Math.floor(localRandomNums.nextDouble(0.99d)*ions.length);
		String[] atom = ions[ionIndex].trim().split(":");
		String symbol = atom[0];
		BigDecimal coefficient = new BigDecimal(atom[1]);
		
		//Finish the question asking for the selected atom or ion		
		String text = String.format("Bereken [%s] als %s mol %s word opgelost in %s L oplosmiddel.",
				symbol,
				mol.toPlainString(), 
				compound.getHtmlFormula(), 
				volume.toPlainString());
		
		// Step 1: moles to moles ion: multiply by the coefficient
		// Step 2: Moles ion to molarity: divide moles by volume
		BigDecimal molIons = mol.multiply(coefficient, fourDigit);
		BigDecimal molarity = molIons.divide(volume, threeDigit).multiply(coefficient, threeDigit);
		String step1 = String.format("%s mol %s x %s = %s mol %s-ionen",
				mol.toPlainString(),
				compound.getHtmlFormula(),
				coefficient.toPlainString(),
				molIons.toPlainString(),
				symbol);
		
		String step2 = String.format("[%s] = %s mol / %s L = %s M", 
				symbol,
				molIons.toPlainString(),
				volume.toPlainString(),
				molarity.toPlainString());
		
		String answer = step1 + lineBreak + step2;
	
		return new Question(text, answer);
	}
}
