package com.chemcalc.practice.domain;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Random;

public class Question {
	
	private Compound compound;			//argument for the Question constructor. Gets set in constructor.
	private Random seededRandomNums;	//argument for the Question constructor. Gets set in constructor.
	private String questionText;		//set in createXX() method. Question type specific.
	private BigDecimal starterAmount;	//set in createXX() method. Question type specific.
	private BigDecimal answerValue;		//set in createXX() method. Question type specific.
	private String answerKeyString;		//set in createXX() method. Question type specific.
	
	private MathContext fiveDigit = new MathContext(5);		//set here - valid for all question types
	private MathContext fourDigit = new MathContext(4);		//set here - valid for all question types
	private MathContext threeDigit = new MathContext(3);	//set here - valid for all question types
	private MathContext twoDigit = new MathContext(2); 		//set here - valid for all question types
	
	public final BigDecimal avogadro = new BigDecimal("6.02214076E23", fiveDigit);
	public final BigDecimal molarVolume = new BigDecimal("0.0245", threeDigit);
	
	public Question(String questiontype, long seed, Compound compound) {
		/* The constructor sets the compound and creates the random
		 * numbers object with the seed. This object is later used
		 * to create random compound amounts. 
		 * 
		 * Then the constructor determines the question type and 
		 * calls the corresponding method. Each type has
		 * its own method for easy debugging and expandability.
		 */
		
		this.compound = compound;
		this.seededRandomNums = new Random(seed);
		
		/* Select which question type is generated randomly for questiontype "Random"
		 * can't use seededRandomNums - messes with reproducibility because the amount of numbers used 
		 * from the stream in question selection varies
		 */
		Random questionSelection = new Random(seed); 
		if (questiontype.equals("Random")) {
			ArrayList<String> questionTypes = new ArrayList<String>();
			questionTypes.add("MolMass");
			questionTypes.add("MassMol");
			questionTypes.add("MolParticles");
			
			if (!compound.getType().equals("metaal") && !compound.getName().equals("water")) {
				//These question types are invalid for metals, in the DB type "metaal"
				//Also invalid specifically for 'water'
				questionTypes.add("MolMolarity"); 
				questionTypes.add("MolarityMol"); 
				questionTypes.add("MolarityVolume");
			}
			
			if (!compound.getType().equals("zout")) {
				questionTypes.add("ParticlesMol");				
			}
			
			if (compound.getPhase().equals("g")) { //These questions only make sense for gasses at 298 K
				questionTypes.add("MolGas");
				questionTypes.add("GasMol"); 
			} else if (compound.getDensity().intValue() != 0) { 
				//Not a gas means mass to volume makes sense at 273 K. 
				//Not all compounds have a listed density, so check it's not zero.
				questionTypes.add("MassVolume"); //TODO
				questionTypes.add("VolumeMass"); //TODO
			}
			
			int r = questionSelection.nextInt(questionTypes.size());
			questiontype = questionTypes.get(r);							
		}
		
		switch(questiontype) {
		case "MassMol":
			this.createMassMol(seed);
			break;
		case "MolMass":
			this.createMolMass(seed);
			break;
		case "MolMolarity":
			this.createMolMolarity(seed);
			break;
		case "MolarityMol":
			this.createMolarityMol(seed);
			break;
		case "MolarityVolume":
			this.createMolarityVolume(seed);
			break;
		case "MolParticles":
			if (compound.getType().equals("metaal")) {
				this.createMolAtomsMetal(seed);
				break;
			} if (compound.getType().equals("zout")) {
				this.createMolAtomsMolecule(seed); //question is so similar these are the same method as molecules
				break;
			} if (compound.getType().equals("moleculair") && questionSelection.nextDouble() < 0.5) {
				this.createMolAtomsMolecule(seed);
				break;
			} else {
				this.createMolMolecules(seed);
				break;
			}
		case "ParticlesMol":
			this.createParticlesMol(seed);
			break;
		case "MolGas":
			this.createMolGas(seed);
			break;
		case "GasMol":
			this.createGasMol(seed);
			break;
		default:
			System.out.println("Invalid question type in Question(String, long, Compound) constructor");
		}
	}
	
	private void createMassMol(long seed) {
		//Adjust multiplication factor here to finetune the answers range
		int factor = 80;
		
		this.starterAmount = new BigDecimal(seededRandomNums.nextDouble()*factor, threeDigit);
		this.questionText = String.format("Bereken hoeveel mol overeenkomt met %s gram %s.", starterAmount.toPlainString(), compound.getHtmlFormula());
		BigDecimal molarMass = new BigDecimal(compound.getMolarMass().toPlainString(), fourDigit); 
		//it's saved with too many characters after the decimal point
		//That's not what should be printed in the answer key. That should work with fourDigit precision
		
		// mass to moles: divide the mass by the molar mass
		this.answerValue = starterAmount.divide(molarMass, threeDigit);
		this.answerKeyString = String.format("%s g / %s g/mol = %s mol %s", 
				starterAmount.toPlainString(), molarMass.toPlainString(), 
				answerValue.toPlainString(), compound.getHtmlFormula());	
	}
	
	private void createMolMass(long seed) {
		int factor = 3;

		this.starterAmount = new BigDecimal(seededRandomNums.nextDouble()*factor, threeDigit);
		this.questionText = String.format("Bereken hoeveel gram overeenkomt met %s mol %s.", starterAmount.toPlainString(), compound.getHtmlFormula());
		BigDecimal molarMass = new BigDecimal(compound.getMolarMass().toPlainString(), fourDigit); 
		//it's saved with too many characters after the decimal point
		//That's not what should be printed in the answer key. That should work with fourDigit precision
		
		// moles to mass: multiply moles by molar mass
		this.answerValue = starterAmount.multiply(molarMass, threeDigit);
		this.answerKeyString = String.format("%s mol x %s g/mol = %s gram %s", 
				starterAmount.toPlainString(), molarMass.toPlainString(), 
				answerValue.toPlainString(), compound.getHtmlFormula());
	}
	
	private void createMolMolarity(long seed) {
		int factorMole = 2;
		int factorVolume = 12;
		
		BigDecimal volume = new BigDecimal(seededRandomNums.nextDouble()*factorVolume, threeDigit);
		this.starterAmount = new BigDecimal(seededRandomNums.nextDouble()*factorMole, threeDigit);
		this.questionText = String.format("Bereken de molariteit van een oplossing van %s mol %s in %s L oplosmiddel.", 
				starterAmount.toPlainString(), compound.getName(), volume.toPlainString());
		
		// moles to molarity: divide moles by volume
		this.answerValue = starterAmount.divide(volume, threeDigit);
		this.answerKeyString = String.format("%s mol / %s L = %s M", 
				starterAmount.toPlainString(), volume.toPlainString(), answerValue.toPlainString());
	}
	
	private void createMolarityMol(long seed) {
		double factorMolarity = 1.5;
		int factorVolume =12;
		
		BigDecimal volume = new BigDecimal(seededRandomNums.nextDouble()*factorVolume, threeDigit);
		this.starterAmount = new BigDecimal(seededRandomNums.nextDouble()*factorMolarity, twoDigit);
		this.questionText = String.format("Bereken hoeveel mol %s nodig is voor %s L oplossing van %s M.", 
				compound.getName(), volume.toPlainString(), starterAmount.toPlainString());
		
		// molarity to moles: multiply molarity by volume
		this.answerValue = starterAmount.multiply(volume, twoDigit);
		this.answerKeyString = String.format("%s M x %s L = %s mol %s", 
				starterAmount.toPlainString(), volume.toPlainString(), answerValue.toPlainString(), compound.getName());
	}
	
	private void createMolarityVolume(long seed) {
		int factorMolarity = 2;
		double factorMole = 1.5;
		
		BigDecimal molarity = new BigDecimal(seededRandomNums.nextDouble()*factorMolarity, threeDigit);
		BigDecimal mole = new BigDecimal(seededRandomNums.nextDouble()*factorMole, threeDigit);
		this.questionText = String.format("Bereken hoeveel oplosmiddel nodig is om van %s mol %s een %s M oplossing te maken.",
					mole.toPlainString(), compound.getName(), molarity.toPlainString());
		
		//volume required: moles divided by molarity
		this.answerValue = mole.divide(molarity, threeDigit);
		this.answerKeyString = String.format("%s mol / %s M = %s L oplosmiddel", 
				mole.toPlainString(), molarity.toPlainString(), answerValue.toPlainString());
	}
	
	private void createMolMolecules(long seed) {
		int factorMole = 2;

		BigDecimal mole = new BigDecimal(seededRandomNums.nextDouble()*factorMole, threeDigit);
		this.questionText = String.format("Bereken hoeveel %s-moleculen %s mol %s bevat.", 
				compound.getHtmlFormula(), mole.toPlainString(), compound.getName());
			
		//mol to particles: multiply mol by avogadro
		this.answerValue = mole.multiply(avogadro, threeDigit);
		this.answerKeyString = String.format("%s mol x %s = %s %s-moleculen", 
				mole.toPlainString(), avogadro.toString().replace("E+23", "x10<sup>23</sup>"),
				answerValue.toString().replace("E+", "x10<sup>").concat("</sup>"), compound.getHtmlFormula());
	}
	
	private void createMolAtomsMolecule(long seed) {
		double factorMole = 1.5;
		
		BigDecimal mole = new BigDecimal(seededRandomNums.nextDouble()*factorMole, threeDigit);
		
		//Grab composition code in the form "X:3, Y:1, Z:1", split, and randomly select an atom or ion
		String composition = compound.getComposition();
		String[] atoms = composition.split(",");
		int atomIndex = (int) Math.floor(seededRandomNums.nextDouble()*atoms.length);
		String[] atom = atoms[atomIndex].trim().split(":");
		String symbol = atom[0];
		BigDecimal coefficient = new BigDecimal(atom[1]);
		
		//Finish the question asking for the selected atom or ion
		this.questionText = String.format("Bereken hoeveel %s-atomen %s mol %s bevat.", 
				symbol, mole.toPlainString(), compound.getHtmlFormula());
		if (compound.getType().equals("zout")) {
			this.questionText = this.questionText.replace("atomen", "ionen");
		}
		
		//mol to atoms: multiply by avogadro and the atom or ion coefficient
		BigDecimal molecules = mole.multiply(avogadro, fourDigit);
		BigDecimal moleIons = mole.multiply(coefficient, threeDigit);
		BigDecimal numberAtoms = molecules.multiply(coefficient, threeDigit);
		
		if (compound.getType().equals("moleculair")) {
			this.answerKeyString = String.format("%s mol x %s = %s moleculen.</br>&nbsp;&nbsp;%s moleculen x %s = %s %s-atomen.", 
					mole.toPlainString(), 
					avogadro.toString().replace("E+23", "x10<sup>23</sup>"), 
					molecules.toString().replace("E+", "x10<sup>").concat("</sup>"), 
					molecules.toString().replace("E+", "x10<sup>").concat("</sup>"),
					coefficient.toPlainString(),
					numberAtoms.toString().replace("E+", "x10<sup>").concat("</sup>"),
					symbol);
		} if (compound.getType().equals("zout")) {
			this.answerKeyString = String.format("%s mol x %s = %s mol %s-ionen</br>&nbsp;&nbsp;%s mol x %s = %s %s-ionen", 
					mole.toPlainString(),
					coefficient.toPlainString(),
					moleIons.toPlainString(),
					symbol,
					moleIons.toPlainString(),
					avogadro.toString().replace("E+23", "x10<sup>23</sup>"),
					numberAtoms.toString().replace("E+", "x10<sup>").concat("</sup>"),
					symbol);
		}
	}
	
	private void createMolAtomsMetal (long seed) {
		int factorMole = 3;
		
		BigDecimal mole = new BigDecimal(seededRandomNums.nextDouble()*factorMole, threeDigit);
		
		this.questionText = String.format("Bereken hoeveel %s-atomen %s mol %s bevat.", 
				compound.getHtmlFormula(), mole.toPlainString(), compound.getName());
		
		//mol to atoms: multiply by avogadro
		BigDecimal numberAtoms = mole.multiply(avogadro, threeDigit);
		this.answerKeyString = String.format("%s mol x %s = %s %s-atomen.",
				mole.toPlainString(), 
				avogadro.toString().replace("E+23", "x10<sup>23</sup>"),
				numberAtoms.toString().replace("E+", "x10<sup>").concat("</sup>"),
				compound.getHtmlFormula());
	}
	
	private void createParticlesMol (long seed) {
		int factorMole = 4;
		
		//Add some variance in digits
		MathContext[] accuracies = {twoDigit, threeDigit, fourDigit};
		int variance = seededRandomNums.nextInt(accuracies.length);
		
		BigDecimal mole = new BigDecimal(seededRandomNums.nextDouble()*factorMole, accuracies[variance]);
		BigDecimal particles = mole.multiply(avogadro, accuracies[variance]);
		
		this.questionText = String.format("Bereken hoeveel mol %s overeenkomt met %s %s-%s.",
				compound.getHtmlFormula(), 
				particles.toString().replace("E+", "x10<sup>").concat("</sup>"),
				compound.getHtmlFormula(),
				compound.getType().equals("moleculair")?"moleculen":"atomen");
		
		//particles to mol: divide by avogadro, but that value is already present as 'mole'
		this.answerKeyString = String.format("%s %s / %s = %s mol %s",
				particles.toString().replace("E+", "x10<sup>").concat("</sup>"),
				compound.getType().equals("moleculair")?"moleculen":"atomen",
				avogadro.toString().replace("E+23", "x10<sup>23</sup>"),
				mole.toPlainString(),
				compound.getHtmlFormula());
	}
	
	private void createMolGas (long seed) {
		int factorMole = 5;
		
		BigDecimal mole = new BigDecimal(seededRandomNums.nextDouble()*factorMole, threeDigit);
		
		this.questionText = String.format("Bereken hoeveel m<sup>3</sup> overeenkomt met %s mol %s (g) bij 298 K.",
				mole.toPlainString(), compound.getHtmlFormula());
		
		//Mol to volume gas: multiply by molar volume constant
		this.answerValue = mole.multiply(molarVolume, threeDigit);
		this.answerKeyString = String.format("%s mol x V<sub>m</sub> = %s mol x %s m<sup>3</sup>/mol = %s m<sup>3</sup>",
				mole.toPlainString(),
				mole.toPlainString(),
				molarVolume.toPlainString(),
				answerValue.toPlainString());
	}
	
	private void createGasMol (long seed) {
		int factorVolume = 40;
		
		BigDecimal volume = new BigDecimal(seededRandomNums.nextDouble()*factorVolume, threeDigit);
		
		this.questionText = String.format("Bereken hoeveel mol overeenkomt met %s L %s (g) bij 298 K.",
				volume.toPlainString(),
				compound.getHtmlFormula());
		
		//volume gas to mol: divide by molar volume constant
		//Attention: this question uses L not m3 as a unit for more sensible mole amounts.
		BigDecimal molarVolLiter = molarVolume.multiply(new BigDecimal(1000), threeDigit);
		this.answerValue = volume.divide(molarVolLiter, 3, RoundingMode.HALF_UP);
		this.answerKeyString = String.format("%s m<sup>3</sup>/mol = %s L/mol</br>&nbsp;&nbsp; %s L / %s L/mol = %s mol %s", 
				molarVolume.toPlainString(),
				molarVolLiter.toPlainString(),
				molarVolLiter.toPlainString(),
				volume.toPlainString(),
				answerValue.toPlainString(),
				compound.getHtmlFormula());
	}

	public String getQuestionText() {
		return questionText;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	public BigDecimal getStarterAmount() {
		return starterAmount;
	}

	public void setStarterAmount(BigDecimal starterAmount) {
		this.starterAmount = starterAmount;
	}

	public BigDecimal getAnswerValue() {
		return answerValue;
	}

	public void setAnswerValue(BigDecimal answerValue) {
		this.answerValue = answerValue;
	}

	public String getAnswerKeyString() {
		return answerKeyString;
	}

	public void setAnswerKeyString(String answerKeyString) {
		this.answerKeyString = answerKeyString;
	}
	
	public Compound getCompound() {
		return compound;
	}

	public void setCompound(Compound compound) {
		this.compound = compound;
	}

	public Random getSeededRandomNums() {
		return seededRandomNums;
	}

	public void setSeededRandomNums(Random seededRandomNums) {
		this.seededRandomNums = seededRandomNums;
	}
	
	
}
