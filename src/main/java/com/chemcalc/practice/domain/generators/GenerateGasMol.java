package com.chemcalc.practice.domain.generators;

import java.util.Random;

import com.chemcalc.practice.domain.Compound;
import com.chemcalc.practice.domain.Question;
import java.math.BigDecimal;

public class GenerateGasMol implements Constants {
	private GenerateGasMol() {};
	
	static public Question create(long subseed, Compound compound) {
		Random localRandomNums = new Random(subseed);
		int factorVolume = 40;
		
		BigDecimal volume = new BigDecimal(localRandomNums.nextDouble()*factorVolume, threeDigit);
		
		String text = String.format("Bereken hoeveel mol overeenkomt met %s L %s (g) bij 298 K",
				volume.toPlainString(),
				compound.getHtmlFormula());
		
		//volume gas to mol: divide mol by molar volume constant
		//Attention: this question uses L not m3 as a unit for more sensible mole amounts.
		BigDecimal molarVolLiter = molarVolume.multiply(new BigDecimal(1000), threeDigit);
		BigDecimal mol = volume.divide(molarVolLiter, threeDigit);
		String answer = String.format("%s m<sup>3</sup>/mol = %s L/mol</br>&nbsp;&nbsp; %s L / %s L/mol = %s mol %s", 
				molarVolume.toPlainString(),
				molarVolLiter.toPlainString(),
				molarVolLiter.toPlainString(),
				volume.toPlainString(),
				mol.toPlainString(),
				compound.getHtmlFormula());
		
		return new Question(text, answer);
	}
}
