package com.chemcalc.practice.domain.generators;

import java.math.BigDecimal;
import java.util.Random;

import com.chemcalc.practice.domain.Compound;
import com.chemcalc.practice.domain.Question;

public class GenerateGasMass implements Constants {
	private GenerateGasMass() {};
	
	static public Question create(long subseed, Compound compound) {
		Random localRandomNums = new Random(subseed);
		int factorVolume = 40;
		
		BigDecimal volume = new BigDecimal(localRandomNums.nextDouble()*factorVolume, threeDigit);
		
		String text = String.format("Bereken de massa van %s L %s (g) bij 298 K",
				volume.toPlainString(),
				compound.getHtmlFormula());
		
		// Step 1: convert Vm from m3/mol to L/mol
		// Step 2: volume gas to mol: divide volume by molar volume constant
		// Step 3: mol to mass: multiply mol by molar mass
		//Attention: this question uses L not m3 as a unit for more sensible mole amounts.
		BigDecimal molarVolLiter = molarVolume.multiply(new BigDecimal(1000), threeDigit);
		BigDecimal mol = volume.divide(molarVolLiter, fourDigit);
		BigDecimal mass = mol.multiply(compound.getMolarMass(), threeDigit);
		
		String step1 = String.format("%s m<sup>3</sup>/mol = %s L/mol", 
				molarVolume.toPlainString(),
				molarVolLiter.toPlainString());
		
		String step2 = String.format("%s L / %s L/mol = %s mol",
				volume.toPlainString(),
				molarVolLiter.toPlainString(),
				mol.toPlainString());
		
		String step3 = String.format("%s mol x %s g/mol = %s g %s",
				mol.toPlainString(),
				compound.getMolarMass().round(fourDigit).toPlainString(),
				mass.toPlainString(),
				compound.getHtmlFormula());
		
		String answer = step1 + lineBreak + step2 + lineBreak + step3;
	
		return new Question(text, answer);
	}
}
