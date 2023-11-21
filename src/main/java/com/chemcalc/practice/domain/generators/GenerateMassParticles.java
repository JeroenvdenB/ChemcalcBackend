package com.chemcalc.practice.domain.generators;

import java.math.BigDecimal;
import java.util.Random;

import com.chemcalc.practice.domain.Compound;
import com.chemcalc.practice.domain.Question;

public class GenerateMassParticles implements Constants {
	private GenerateMassParticles() {};
	
	static public Question create(long subseed, Compound compound) {
		Random localRandomNums = new Random(subseed);
		
		// Further divide question type based on the compound
		if (compound.getType().equals("metaal")) {
			return createMassAtomsMetal(localRandomNums, compound);
		} if (compound.getType().equals("zout")) {
			return createMassAtomsMolecule(localRandomNums, compound);
		} if (compound.getType().equals("moleculair") && localRandomNums.nextDouble() < 0.5) {
			return createMassAtomsMolecule(localRandomNums, compound);
		} else {
			return createMassMolecules(localRandomNums, compound);
		}

	}
	
	static private Question createMassAtomsMetal(Random localRandomNums, Compound compound) {
		int factorMol = 3;
		
		BigDecimal mol = new BigDecimal(localRandomNums.nextDouble()*factorMol, fourDigit);
		BigDecimal mass = mol.multiply(compound.getMolarMass(), threeDigit);
		BigDecimal numberAtoms = mol.multiply(avogadro, threeDigit);
		
		String text = String.format("Bereken hoeveel %s-atomen %s gram %s bevat", 
				compound.getHtmlFormula(), 
				mass.toPlainString(), 
				compound.getName());
		
		//Step 1: mass to mol: divide mass by molar mass
		//Step 2: mol to atoms: multiply by avogadro
		String step1 = String.format("%s g / %s g/mol = %s mol %s",
				mass.toPlainString(),
				compound.getMolarMass().round(fourDigit).toPlainString(),
				mol.toPlainString(),
				compound.getHtmlFormula());
		String step2 = String.format("%s mol x N<sub>A</sub> = %s x %s = %s %s-atomen",
				mol.toPlainString(),
				mol.toPlainString(),
				avogadro.toString().replace("E+23", "x10<sup>23</sup>"),
				numberAtoms.toString().replace("E+", "x10<sup>").concat("</sup>"),
				compound.getHtmlFormula());
		
		String answer = step1 + lineBreak + step2;
		
		return new Question(text, answer);
	}
	
	static private Question createMassAtomsMolecule(Random localRandomNums, Compound compound) {
		double factorMol = 1.3;
		
		BigDecimal mol = new BigDecimal(localRandomNums.nextDouble()*factorMol, fourDigit);
		BigDecimal mass = mol.multiply(compound.getMolarMass(), threeDigit);
		
		//Grab composition code in the form "X:3, Y:1, Z:1", split, and randomly select an atom or ion
		String composition = compound.getComposition();
		String[] atoms = composition.split(",");
		int atomIndex = (int) Math.floor(localRandomNums.nextDouble(0.99d)*atoms.length);
		String[] atom = atoms[atomIndex].trim().split(":");
		String symbol = atom[0];
		BigDecimal coefficient = new BigDecimal(atom[1]);
		
		String text = String.format("Bereken hoeveel %s-atomen %s gram %s bevat", 
				symbol,
				mass.toPlainString(), 
				compound.getHtmlFormula());
		
		//Step 1: mass to mol: divide mass by molar mass
		//Step 2: mol to ions/atoms: multiply mol compound by coefficient
		//Step 3: mol ions/atoms to atoms: multiply by avogadro
		BigDecimal molecules = mol.multiply(avogadro, fourDigit);
		BigDecimal molAtoms = mol.multiply(coefficient, threeDigit);
		BigDecimal numberAtoms = molecules.multiply(coefficient, threeDigit);

		String step1 = String.format("%s g / %s g/mol = %s mol %s",
				mass.toPlainString(),
				compound.getMolarMass().round(fourDigit).toPlainString(),
				mol.toPlainString(),
				compound.getHtmlFormula());
		String step2 = String.format("%s mol %s x %s = %s mol %s-atomen",
				mol.toPlainString(),
				compound.getHtmlFormula(),
				coefficient.toPlainString(),
				molAtoms.toPlainString(),
				symbol);
		String step3 = String.format("%s mol x N<sub>A</sub> = %s mol x %s = %s %s-atomen",
				molAtoms.toPlainString(),
				molAtoms.toPlainString(),
				avogadro.toString().replace("E+23", "x10<sup>23</sup>"),
				numberAtoms.toString().replace("E+", "x10<sup>").concat("</sup>"),
				symbol);
			
		String answer = step1 + lineBreak + step2 + lineBreak + step3;
			
		if (compound.getType().equals("zout")) {
			text = text.replace("atomen", "ionen");
			answer = answer.replace("atomen", "ionen");
		}
		
		return new Question(text, answer);
	}
	
	static private Question createMassMolecules(Random localRandomNums, Compound compound) {
		int factorMol = 4;
		
		BigDecimal mol = new BigDecimal(localRandomNums.nextDouble()*factorMol, fourDigit);
		BigDecimal mass = mol.multiply(compound.getMolarMass(), threeDigit);
		BigDecimal molecules = mol.multiply(avogadro, threeDigit);
		
		String text = String.format("Bereken hoeveel %s-moleculen %s gram %s bevat", 
				compound.getHtmlFormula(), 
				mass.toPlainString(), 
				compound.getName());
		
		//Step 1: mass to mol: divide mass by molar mass
		//Step 2: mol to molecules: multiply by avogadro
		String step1 = String.format("%s g / %s g/mol = %s mol %s",
				mass.toPlainString(),
				compound.getMolarMass().round(fourDigit).toPlainString(),
				mol.toPlainString(),
				compound.getHtmlFormula());
		String step2 = String.format("%s mol x N<sub>A</sub> = %s x %s = %s %s-moleculen",
				mol.toPlainString(),
				mol.toPlainString(),
				avogadro.toString().replace("E+23", "x10<sup>23</sup>"),
				molecules.toString().replace("E+", "x10<sup>").concat("</sup>"),
				compound.getHtmlFormula());
		
		String answer = step1 + lineBreak + step2;
		
		return new Question(text, answer);
	}
	
	
}
