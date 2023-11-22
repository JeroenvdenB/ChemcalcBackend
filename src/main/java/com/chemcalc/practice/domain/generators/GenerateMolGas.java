package com.chemcalc.practice.domain.generators;

import java.math.BigDecimal;
import java.util.Random;

import com.chemcalc.practice.domain.Compound;
import com.chemcalc.practice.domain.Question;

public class GenerateMolGas implements Constants{
	private GenerateMolGas() {};
	
	static public Question create(long subseed, Compound compound) {
		Random localRandomNums = new Random(subseed);
			int factorMol = 10;
			
			BigDecimal mol = new BigDecimal(localRandomNums.nextDouble()*factorMol, threeDigit);
			
			String text = String.format("Bereken hoeveel m<sup>3</sup> overeenkomt met %s mol %s (g) bij 298 K",
					mol.toPlainString(), compound.getHtmlFormula());
			
			//Mol to volume gas: multiply by molar volume constant
			BigDecimal gas = mol.multiply(molarVolume, threeDigit);
			String answer = String.format("%s mol x V<sub>m</sub> = %s mol x %s m<sup>3</sup>/mol = %s m<sup>3</sup>",
					mol.toPlainString(),
					mol.toPlainString(),
					molarVolume.toPlainString(),
					gas.toPlainString());
		return new Question(text, answer);
	}
}
