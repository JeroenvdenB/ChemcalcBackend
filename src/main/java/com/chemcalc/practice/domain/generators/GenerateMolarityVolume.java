package com.chemcalc.practice.domain.generators;

import java.util.Random;

import com.chemcalc.practice.domain.Compound;
import com.chemcalc.practice.domain.Question;
import java.math.BigDecimal;

public class GenerateMolarityVolume implements Constants {
	private GenerateMolarityVolume() {};
	
	static public Question create(long subseed, Compound compound) {
		Random localRandomNums = new Random(subseed);
		int factorMolarity = 2;
		double factorMol = 1.5;
		
		BigDecimal molarity = new BigDecimal(localRandomNums.nextDouble()*factorMolarity, threeDigit);
		BigDecimal mol = new BigDecimal(localRandomNums.nextDouble()*factorMol, threeDigit);
		String text = String.format("Bereken hoeveel oplosmiddel nodig is om van %s mol %s een %s M oplossing te maken",
					mol.toPlainString(), 
					compound.getName(), 
					molarity.toPlainString());
		
		//Volume required: moles divided by molarity
		BigDecimal volume = mol.divide(molarity, threeDigit);
		String answer = String.format("%s mol / %s M = %s L oplosmiddel", 
				mol.toPlainString(), 
				molarity.toPlainString(), 
				volume.toPlainString());
	
		return new Question(text, answer);
	}
}
