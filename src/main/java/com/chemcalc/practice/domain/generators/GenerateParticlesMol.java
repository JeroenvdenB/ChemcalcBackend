package com.chemcalc.practice.domain.generators;

import java.util.Random;

import com.chemcalc.practice.domain.Compound;
import com.chemcalc.practice.domain.Question;
import java.math.BigDecimal;
import java.math.MathContext;

public class GenerateParticlesMol implements Constants{
	private GenerateParticlesMol() {};
	
	static public Question create(long subseed, Compound compound) {
		Random localRandomNums = new Random(subseed);
			int factorMol = 4;
			
			//Add some variance in digits
			MathContext[] accuracies = {twoDigit, threeDigit, fourDigit};
			int variance = localRandomNums.nextInt(accuracies.length);
			
			BigDecimal mole = new BigDecimal(localRandomNums.nextDouble()*factorMol, accuracies[variance]);
			BigDecimal particles = mole.multiply(avogadro, accuracies[variance]);
			
			String text = String.format("Bereken hoeveel mol %s overeenkomt met %s %s-%s",
					compound.getHtmlFormula(), 
					particles.toString().replace("E+", "x10<sup>").concat("</sup>"),
					compound.getHtmlFormula(),
					compound.getType().equals("moleculair")?"moleculen":"atomen");
			
			//particles to mol: divide by avogadro, but that value is already present as 'mole'
			String answer = String.format("%s %s / %s = %s mol %s",
					particles.toString().replace("E+", "x10<sup>").concat("</sup>"),
					compound.getType().equals("moleculair")?"moleculen":"atomen",
					avogadro.toString().replace("E+23", "x10<sup>23</sup>"),
					mole.toPlainString(),
					compound.getHtmlFormula());
		
		return new Question(text, answer);
	}
}
