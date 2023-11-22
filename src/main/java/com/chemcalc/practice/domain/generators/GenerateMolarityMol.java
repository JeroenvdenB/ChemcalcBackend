package com.chemcalc.practice.domain.generators;

import java.math.BigDecimal;
import java.util.Random;

import com.chemcalc.practice.domain.Compound;
import com.chemcalc.practice.domain.Question;

public class GenerateMolarityMol implements Constants {
	private GenerateMolarityMol() {};
	
	static public Question create(long subseed, Compound compound) {
		Random localRandomNums = new Random(subseed);
		int factorVolume =12;
		double factorMolarity = 1.5;
		
		BigDecimal volume = new BigDecimal(localRandomNums.nextDouble()*factorVolume, threeDigit);
		BigDecimal molarity = new BigDecimal(localRandomNums.nextDouble()*factorMolarity, twoDigit);
		
		String text = String.format("Bereken hoeveel mol %s nodig is voor %s L oplossing van %s M", 
				compound.getName(), 
				volume.toPlainString(), 
				molarity.toPlainString());
		
		// molarity to moles: multiply molarity by volume
		BigDecimal mol = molarity.multiply(volume, twoDigit);
		String answer = String.format("%s M x %s L = %s mol %s", 
				molarity.toPlainString(), 
				volume.toPlainString(), 
				mol.toPlainString(), 
				compound.getName());
	
		return new Question(text, answer);
	}
}
