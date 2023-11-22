package com.chemcalc.practice.domain.generators;

import java.math.BigDecimal;
import java.util.Random;

import com.chemcalc.practice.domain.Compound;
import com.chemcalc.practice.domain.Question;

public class GenerateMolMolarity implements Constants {
	private GenerateMolMolarity() {};
	
	static public Question create(long subseed, Compound compound) {
		Random localRandomNums = new Random(subseed);
		int factorMole = 2;
		int factorVolume = 12;
		
		BigDecimal volume = new BigDecimal(localRandomNums.nextDouble()*factorVolume, threeDigit);
		BigDecimal mol = new BigDecimal(localRandomNums.nextDouble()*factorMole, threeDigit);
		
		String text = String.format("Bereken de molariteit van een oplossing van %s mol %s in %s L oplosmiddel", 
				mol.toPlainString(), 
				compound.getHtmlFormula(), 
				volume.toPlainString());
		
		// moles to molarity: divide moles by volume
		BigDecimal molarity = mol.divide(volume, threeDigit);
		String answer = String.format("%s mol / %s L = %s M", 
				mol.toPlainString(), 
				volume.toPlainString(), 
				molarity.toPlainString());
	
		return new Question(text, answer);
	}
}
