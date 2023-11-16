package com.chemcalc.practice.domain.generators;

import java.math.BigDecimal;
import java.util.Random;

import com.chemcalc.practice.domain.Compound;
import com.chemcalc.practice.domain.Question;

public class GenerateMolarityMass implements Constants {
	private GenerateMolarityMass() {};
	
	static public Question create(long subseed, Compound compound) {
		Random localRandomNums = new Random(subseed);
		int factorVolume =12;
		double factorMolarity = 1.5;
		
		BigDecimal volume = new BigDecimal(localRandomNums.nextDouble()*factorVolume, threeDigit);
		BigDecimal molarity = new BigDecimal(localRandomNums.nextDouble()*factorMolarity, twoDigit);
		
		String text = String.format("Bereken hoeveel gram %s nodig is voor %s L oplossing van %s M", 
				compound.getName(), 
				volume.toPlainString(), 
				molarity.toPlainString());
		
		// Step 1: molarity to mol: multiply molarity by volume
		// Step 2: mol to mass: multiply mol by molar mass
		BigDecimal mol = molarity.multiply(volume, threeDigit);
		BigDecimal mass = mol.multiply(compound.getMolarMass(), twoDigit);
		String step1 = String.format("%s M x %s L = %s mol %s", 
				molarity.toPlainString(), 
				volume.toPlainString(), 
				mol.toPlainString(), 
				compound.getName());
		String step2 = String.format("%s mol x %s g/mol = %s g %s",
				mol.toPlainString(),
				compound.getMolarMass().round(fourDigit).toPlainString(),
				mass.toString().replace("E+", "x10<sup>").concat("</sup>"),
				compound.getName());
		String answer = step1 + lineBreak + step2;
		
		return new Question(text, answer);
	}
}
