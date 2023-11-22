package com.chemcalc.practice.domain.generators;

import java.math.BigDecimal;
import java.util.Random;

import com.chemcalc.practice.domain.Compound;
import com.chemcalc.practice.domain.Question;

public class GenerateParticlesMass implements Constants {
	private GenerateParticlesMass() {};
	
	static public Question create(long subseed, Compound compound) {
		int factorMol = 3;
		Random localRandomNums = new Random(subseed);
		
		BigDecimal mol = new BigDecimal(localRandomNums.nextDouble()*factorMol, fourDigit);
		BigDecimal mass = mol.multiply(compound.getMolarMass(), threeDigit);
		BigDecimal particles = mol.multiply(avogadro, threeDigit);
		
		String text = String.format("Bereken de massa van %s %s-moleculen", //replace to "atomen" later if molecular compound
				particles.toString().replace("E+", "x10<sup>").concat("</sup>"),
				compound.getHtmlFormula());
		
		//Step 1: particles to mol: divide by avogadro
		//Step 2: mol to mass: mulitply by molar mass
		String step1 = String.format("%s moleculen / N<sub>A</sub> = %s / %s = %s mol",
				particles.toString().replace("E+", "x10<sup>").concat("</sup>"),
				particles.toString().replace("E+", "x10<sup>").concat("</sup>"),
				avogadro.toString().replace("E+23", "x10<sup>23</sup>"),
				mol.toPlainString());
		String step2 = String.format("%s mol x %s g/mol = %s g %s",
				mol.toPlainString(),
				compound.getMolarMass().round(fourDigit).toPlainString(),
				mass.toPlainString(),
				compound.getHtmlFormula());
		
		String answer = step1 + lineBreak + step2;
	
		// To check, grab the composition, split it, and check the coefficient. There should be only one
		// component, and the coefficient is 1 for all metals and mono-atomic molecular compounds.
		// If it is mono-atomic or a metal, 'molecules' should change to 'atoms'.
		String[] components = compound.getComposition().split(",");
		char coefficient = components[0].charAt(components[0].length()-1); // This does not account for double-digit coefficients. 
		if (components.length == 1 && coefficient == '1') {
			text = text.replace("moleculen", "atomen");
			answer = answer.replace("moleculen", "atomen");
		}		
		
		return new Question(text, answer);
	}
}
