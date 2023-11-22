package com.chemcalc.practice.domain.generators;

import java.util.Random;

import com.chemcalc.practice.domain.Compound;
import com.chemcalc.practice.domain.Question;
import java.math.BigDecimal;

public class GenerateMolParticles implements Constants {
	private GenerateMolParticles() {};
	
	static public Question create(long subseed, Compound compound) {
		Random localRandomNums = new Random(subseed);
		
		if (compound.getType().equals("metaal")) {
			return createMolAtomsMetal(localRandomNums, compound);
		} if (compound.getType().equals("zout") || (compound.getType().equals("moleculair") && localRandomNums.nextDouble() < 0.5)) {
			return createMolAtomsMolecule(localRandomNums, compound);
		} else {
			return createMolMolecules(localRandomNums, compound);
		}
		
	}
	
	static private Question createMolAtomsMetal (Random localRandomNums, Compound compound) {
		int factorMol = 3;
		
		BigDecimal mol = new BigDecimal(localRandomNums.nextDouble()*factorMol, threeDigit);
		
		String text = String.format("Bereken hoeveel %s-atomen %s mol %s bevat", 
				compound.getHtmlFormula(), 
				mol.toPlainString(), 
				compound.getName());
		
		//mol to atoms: multiply mol by avogadro
		BigDecimal numberAtoms = mol.multiply(avogadro, threeDigit);
		String answer = String.format("%s mol x %s = %s %s-atomen",
				mol.toPlainString(), 
				avogadro.toString().replace("E+23", "x10<sup>23</sup>"),
				numberAtoms.toString().replace("E+", "x10<sup>").concat("</sup>"),
				compound.getHtmlFormula());
		
		return new Question(text, answer);
	}
	
	static private Question createMolAtomsMolecule (Random localRandomNums, Compound compound) {
		double factorMol = 1.5;
		
		BigDecimal mol = new BigDecimal(localRandomNums.nextDouble()*factorMol, threeDigit);
		
		//Grab composition code in the form "X:3, Y:1, Z:1", split, and randomly select an atom or ion
		String composition = compound.getComposition();
		String[] atoms = composition.split(",");
		int atomIndex = (int) Math.floor(localRandomNums.nextDouble(0.99d)*atoms.length);
		String[] atom = atoms[atomIndex].trim().split(":");
		String symbol = atom[0];
		BigDecimal coefficient = new BigDecimal(atom[1]);
		
		//Finish the question asking for the selected atom or ion
		String text = String.format("Bereken hoeveel %s-%s %s mol %s bevat",  //TODO: turn this into 'atomen' and replace later if salt, together with answer repl.
				symbol,
				(compound.getType().equals("zout")) ? "ionen" : "atomen",
				mol.toPlainString(), 
				compound.getHtmlFormula());
		
		// Step 1: mol compound to mol atoms or ions: multiply by coefficient
		// Step 2: mol to number of ions or atoms: multiply mol by avogadro
		BigDecimal molAtoms = mol.multiply(coefficient, threeDigit);
		BigDecimal numberAtoms = molAtoms.multiply(coefficient, threeDigit);
		
		String step1 = String.format("%s mol %s x %s = %s mol %s-atomen",
				mol.toPlainString(),
				compound.getHtmlFormula(),
				coefficient.toPlainString(),
				molAtoms.toPlainString(),
				symbol);
		
		String step2 = String.format("%s mol x N<sub>A</sub> = %s x %s = %s %s-atomen", 
				molAtoms.toPlainString(),
				molAtoms.toPlainString(),
				avogadro.toString().replace("E+23", "x10<sup>23</sup>"),
				numberAtoms.toString().replace("E+", "x10<sup>").concat("</sup>"),
				symbol);
		
		String answer = step1 + lineBreak + step2;
		
		// Replace the word atoms if it's a salt. That should be called ions instead.
		if (compound.getType().equals("zout")) {
			text = text.replace("atomen", "ionen");
			answer = answer.replace("atomen", "ionen");
		}
		
		return new Question(text, answer);		
	}
	
	static private Question createMolMolecules (Random localRandomNums, Compound compound) {
		int factorMol = 2;

		BigDecimal mol = new BigDecimal(localRandomNums.nextDouble()*factorMol, threeDigit);
		String text = String.format("Bereken hoeveel %s-moleculen %s mol %s bevat", 
				compound.getHtmlFormula(), 
				mol.toPlainString(), 
				compound.getName());
			
		//mol to particles: multiply mol by avogadro
		BigDecimal molecules = mol.multiply(avogadro, threeDigit);
		String answer = String.format("%s mol x %s = %s %s-moleculen", 
				mol.toPlainString(), 
				avogadro.toString().replace("E+23", "x10<sup>23</sup>"),
				molecules.toString().replace("E+", "x10<sup>").concat("</sup>"), 
				compound.getHtmlFormula());
		
		return new Question(text, answer);	
	}
}
