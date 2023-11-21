package com.chemcalc.practice.domain.generators;

import java.math.BigDecimal;
import java.util.Random;

import com.chemcalc.practice.domain.Compound;
import com.chemcalc.practice.domain.Question;

public class GenerateMolarityIonsMass implements Constants {
	private GenerateMolarityIonsMass() {};
	
	static public Question create(long subseed, Compound compound) {
		Random localRandomNums = new Random(subseed);
		int factorVolume =12;
		double factorMolarity = 0.9;
		
		BigDecimal volume = new BigDecimal(localRandomNums.nextDouble()*factorVolume, threeDigit);
		BigDecimal molarity = new BigDecimal(localRandomNums.nextDouble()*factorMolarity, twoDigit);
		
		//Grab composition code in the form "X:3, Y:1, Z:1", split, and randomly select an ion
		String composition = compound.getComposition();
		String[] ions = composition.split(",");
		int ionIndex = (int) Math.floor(localRandomNums.nextDouble(0.99d)*ions.length);
		String[] atom = ions[ionIndex].trim().split(":");
		String symbol = atom[0];
		BigDecimal coefficient = new BigDecimal(atom[1]);
		
		//Finish the question given the concentration of a certain ion	
		String text = String.format("Bereken hoeveel gram %s nodig is om %s L oplossing te maken met [%s]=%s M",
				compound.getHtmlFormula(),
				volume.toPlainString(),
				symbol,
				molarity.toPlainString());
		
		// Step 1: molarity to mol ions: multiply molarity by volume
		// Step 2: mol ions to mol salt: divide by coefficient
		// Step 3: mol salt to mass: multiply mol by molar mass
		BigDecimal molIons = molarity.multiply(volume, threeDigit);
		BigDecimal molSalt = molIons.divide(coefficient, threeDigit);
		BigDecimal mass = molSalt.multiply(compound.getMolarMass(), twoDigit);
		String step1 = String.format("%s M x %s L = %s mol %s-ionen", 
				molarity.toPlainString(), 
				volume.toPlainString(), 
				molIons.toPlainString(), 
				symbol);
		String step2 = String.format("%s mol / %s = %s mol %s", 
				molIons.toPlainString(),
				coefficient.toPlainString(),
				molSalt.toPlainString(),
				compound.getHtmlFormula());
		String step3 = String.format("%s mol x %s g/mol = %s g %s",
				molSalt.toPlainString(),
				compound.getMolarMass().round(fourDigit).toPlainString(),
				mass.toString().replace("E+", "x10<sup>").concat("</sup>"),
				compound.getName());
		String answer = step1 + lineBreak + step2 + lineBreak + step3;
		
		return new Question(text, answer);
	}
}
