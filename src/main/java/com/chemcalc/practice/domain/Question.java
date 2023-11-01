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
	private String answerKeyString;		//set in createXX() method. Question type specific.
	
	private MathContext fiveDigit = new MathContext(5);		//set here - valid for all question types
	private MathContext fourDigit = new MathContext(4);		//set here - valid for all question types
	private MathContext threeDigit = new MathContext(3);	//set here - valid for all question types
	private MathContext twoDigit = new MathContext(2); 		//set here - valid for all question types
	
	public final BigDecimal avogadro = new BigDecimal("6.02214076E23", fiveDigit);
	public final BigDecimal molarVolume = new BigDecimal("0.0245", threeDigit);
	
	public Question(String questionType, long seed, Compound compound) {
		/* The constructor sets the compound and creates the random
		 * numbers object with the seed. This random object is later used
		 * to create random question parameters. 
		 * 
		 * If the type is "Random", the constructor will determine which question types
		 * are suitable for the given compound. It will then pick a random question type
		 * and call the corresponding method.
		 * 
		 * If the type is not random, care must be taken to match the question type
		 * with a suitable compound. No errors are thrown upon a mismatch, but it will
		 * generate physically impossible questions!
		 * 
		 * Other supported types are:
		 * "1-step" questions:
		 * 		Mol to mass (any compound)
		 * 		Mass to mol (any compound)
		 * 		Mol to particles (any compound)
		 * 		Mol to molarity (any non-metal except water)
		 * 		Molarity to mol (any non-metal except water)
		 * 		Molarity to volume required (any non-metal, except water)
		 * 		Particles to mol (any non-salt)
		 * 		Mol to gas volume (any gas)
		 * 		Gas volume to mol (any gas)
		 * 		Mass to volume (any non-gas with a density)
		 * 		Volume to mass (any non-gas with a density)
		 * 
		 * "2-step" questions:
		 * 		Molarity to mass (any compound)
		 * 		Mass to molarity (any compound)
		 * 		Mass to particles (any compound)
		 * 		Particles to mass (any non-salt)
		 * 		Volume to mol (any non-gas with a density)
		 * 		Mol to volume (any non-gas with a density)
		 * 		Particles to molarity (???)
		 * 		Molarity to particles (???)
		 * 
		 */
		
		this.compound = compound;
		this.seededRandomNums = new Random(seed);
		
		//random question selection if questionType is "Random"
		// can't use seededRandomNums - messes with reproducibility because the amount of numbers used from the stream
		Random questionSelection = new Random(seed); 
		
		if (questionType.equals("Random")) {
			
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
				questionTypes.add("MassVolume"); 
				questionTypes.add("VolumeMass"); 
			}
				
			int r = questionSelection.nextInt(questionTypes.size());
			questionType = questionTypes.get(r);							
		}
		
		switch(questionType) {
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
		case "MassVolume":
			this.createMassVolume(seed);
			break;
		case "VolumeMass":
			this.createVolumeMass(seed);
			break;
		default:
			System.out.println("Invalid question type in Question(String, long, Compound) constructor");
		}
	}
	
	private void createMassMol(long seed) {
		//Adjust multiplication factor here to finetune the answers range
		int factorMass = 80;
		
		BigDecimal mass = new BigDecimal(seededRandomNums.nextDouble()*factorMass, threeDigit);
		this.questionText = String.format("Bereken hoeveel mol overeenkomt met %s gram %s", 
				mass.toPlainString(), 
				compound.getHtmlFormula());

		//Molar mass is saved with too many characters after the decimal point
		//That's not what should be printed in the answer key. The answer key must have fourDigit precision.
		BigDecimal molarMass = new BigDecimal(compound.getMolarMass().toPlainString(), fourDigit); 
		
		// mass to moles: divide the mass by the molar mass
		BigDecimal mol = mass.divide(molarMass, threeDigit);
		this.answerKeyString = String.format("%s g / %s g/mol = %s mol %s", 
				mass.toPlainString(), molarMass.toPlainString(), 
				mol.toPlainString(), compound.getHtmlFormula());	
	}
	
	private void createMolMass(long seed) {
		int factor = 3;

		BigDecimal mol = new BigDecimal(seededRandomNums.nextDouble()*factor, threeDigit);
		this.questionText = String.format("Bereken hoeveel gram overeenkomt met %s mol %s", 
				mol.toPlainString(), 
				compound.getHtmlFormula());
		
		//Molar mass is saved with too many characters after the decimal point
		//That's not what should be printed in the answer key. That should work with fourDigit precision
		BigDecimal molarMass = new BigDecimal(compound.getMolarMass().toPlainString(), fourDigit); 
		
		// moles to mass: multiply moles by molar mass
		BigDecimal mass = mol.multiply(molarMass, threeDigit);
		this.answerKeyString = String.format("%s mol x %s g/mol = %s gram %s", 
				mol.toPlainString(), molarMass.toPlainString(), 
				mass.toPlainString(), compound.getHtmlFormula());
	}
	
	private void createMolMolarity(long seed) {
		int factorMole = 2;
		int factorVolume = 12;
		
		BigDecimal volume = new BigDecimal(seededRandomNums.nextDouble()*factorVolume, threeDigit);
		BigDecimal mol = new BigDecimal(seededRandomNums.nextDouble()*factorMole, threeDigit);
		
		this.questionText = String.format("Bereken de molariteit van een oplossing van %s mol %s in %s L oplosmiddel", 
				mol.toPlainString(), 
				compound.getName(), 
				volume.toPlainString());
		
		// moles to molarity: divide moles by volume
		BigDecimal molarity = mol.divide(volume, threeDigit);
		this.answerKeyString = String.format("%s mol / %s L = %s M", 
				mol.toPlainString(), 
				volume.toPlainString(), 
				molarity.toPlainString());
	}
	
	private void createMolarityMol(long seed) {
		int factorVolume =12;
		double factorMolarity = 1.5;
		
		BigDecimal volume = new BigDecimal(seededRandomNums.nextDouble()*factorVolume, threeDigit);
		BigDecimal molarity = new BigDecimal(seededRandomNums.nextDouble()*factorMolarity, twoDigit);
		
		this.questionText = String.format("Bereken hoeveel mol %s nodig is voor %s L oplossing van %s M", 
				compound.getName(), 
				volume.toPlainString(), 
				molarity.toPlainString());
		
		// molarity to moles: multiply molarity by volume
		BigDecimal mol = molarity.multiply(volume, twoDigit);
		this.answerKeyString = String.format("%s M x %s L = %s mol %s", 
				molarity.toPlainString(), 
				volume.toPlainString(), 
				mol.toPlainString(), 
				compound.getName());
	}
	
	private void createMolarityVolume(long seed) {
		int factorMolarity = 2;
		double factorMol = 1.5;
		
		BigDecimal molarity = new BigDecimal(seededRandomNums.nextDouble()*factorMolarity, threeDigit);
		BigDecimal mol = new BigDecimal(seededRandomNums.nextDouble()*factorMol, threeDigit);
		this.questionText = String.format("Bereken hoeveel oplosmiddel nodig is om van %s mol %s een %s M oplossing te maken",
					mol.toPlainString(), compound.getName(), molarity.toPlainString());
		
		//volume required: moles divided by molarity
		BigDecimal volume = mol.divide(molarity, threeDigit);
		this.answerKeyString = String.format("%s mol / %s M = %s L oplosmiddel", 
				mol.toPlainString(), 
				molarity.toPlainString(), 
				volume.toPlainString());
	}
	
	private void createMolMolecules(long seed) {
		int factorMol = 2;

		BigDecimal mol = new BigDecimal(seededRandomNums.nextDouble()*factorMol, threeDigit);
		this.questionText = String.format("Bereken hoeveel %s-moleculen %s mol %s bevat", 
				compound.getHtmlFormula(), 
				mol.toPlainString(), 
				compound.getName());
			
		//mol to particles: multiply mol by avogadro
		BigDecimal molecules = mol.multiply(avogadro, threeDigit);
		this.answerKeyString = String.format("%s mol x %s = %s %s-moleculen", 
				mol.toPlainString(), 
				avogadro.toString().replace("E+23", "x10<sup>23</sup>"),
				molecules.toString().replace("E+", "x10<sup>").concat("</sup>"), 
				compound.getHtmlFormula());
	}
	
	private void createMolAtomsMolecule(long seed) {
		double factorMol = 1.5;
		
		BigDecimal mol = new BigDecimal(seededRandomNums.nextDouble()*factorMol, threeDigit);
		
		//Grab composition code in the form "X:3, Y:1, Z:1", split, and randomly select an atom or ion
		String composition = compound.getComposition();
		String[] atoms = composition.split(",");
		int atomIndex = (int) Math.floor(seededRandomNums.nextDouble()*atoms.length);
		String[] atom = atoms[atomIndex].trim().split(":");
		String symbol = atom[0];
		BigDecimal coefficient = new BigDecimal(atom[1]);
		
		//Finish the question asking for the selected atom or ion
		this.questionText = String.format("Bereken hoeveel %s-atomen %s mol %s bevat", 
				symbol, 
				mol.toPlainString(), 
				compound.getHtmlFormula());
		if (compound.getType().equals("zout")) {
			this.questionText = this.questionText.replace("atomen", "ionen");
		}
		
		//mol to atoms: multiply by avogadro and the atom or ion coefficient
		BigDecimal molecules = mol.multiply(avogadro, fourDigit);
		BigDecimal molIons = mol.multiply(coefficient, threeDigit);
		BigDecimal numberAtoms = molecules.multiply(coefficient, threeDigit);
		
		if (compound.getType().equals("moleculair")) {
			this.answerKeyString = String.format("%s mol x %s = %s moleculen.</br>&nbsp;&nbsp;%s moleculen x %s = %s %s-atomen", 
					mol.toPlainString(), 
					avogadro.toString().replace("E+23", "x10<sup>23</sup>"), 
					molecules.toString().replace("E+", "x10<sup>").concat("</sup>"), 
					molecules.toString().replace("E+", "x10<sup>").concat("</sup>"),
					coefficient.toPlainString(),
					numberAtoms.toString().replace("E+", "x10<sup>").concat("</sup>"),
					symbol);
		} if (compound.getType().equals("zout")) {
			this.answerKeyString = String.format("%s mol x %s = %s mol %s-ionen</br>&nbsp;&nbsp;%s mol x %s = %s %s-ionen", 
					mol.toPlainString(),
					coefficient.toPlainString(),
					molIons.toPlainString(),
					symbol,
					molIons.toPlainString(),
					avogadro.toString().replace("E+23", "x10<sup>23</sup>"),
					numberAtoms.toString().replace("E+", "x10<sup>").concat("</sup>"),
					symbol);
		}
	}
	
	private void createMolAtomsMetal (long seed) {
		int factorMol = 3;
		
		BigDecimal mol = new BigDecimal(seededRandomNums.nextDouble()*factorMol, threeDigit);
		
		this.questionText = String.format("Bereken hoeveel %s-atomen %s mol %s bevat", 
				compound.getHtmlFormula(), 
				mol.toPlainString(), 
				compound.getName());
		
		//mol to atoms: multiply by avogadro
		BigDecimal numberAtoms = mol.multiply(avogadro, threeDigit);
		this.answerKeyString = String.format("%s mol x %s = %s %s-atomen",
				mol.toPlainString(), 
				avogadro.toString().replace("E+23", "x10<sup>23</sup>"),
				numberAtoms.toString().replace("E+", "x10<sup>").concat("</sup>"),
				compound.getHtmlFormula());
	}
	
	private void createParticlesMol (long seed) {
		int factorMol = 4;
		
		//Add some variance in digits
		MathContext[] accuracies = {twoDigit, threeDigit, fourDigit};
		int variance = seededRandomNums.nextInt(accuracies.length);
		
		BigDecimal mole = new BigDecimal(seededRandomNums.nextDouble()*factorMol, accuracies[variance]);
		BigDecimal particles = mole.multiply(avogadro, accuracies[variance]);
		
		this.questionText = String.format("Bereken hoeveel mol %s overeenkomt met %s %s-%s",
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
		int factorMol = 5;
		
		BigDecimal mol = new BigDecimal(seededRandomNums.nextDouble()*factorMol, threeDigit);
		
		this.questionText = String.format("Bereken hoeveel m<sup>3</sup> overeenkomt met %s mol %s (g) bij 298 K",
				mol.toPlainString(), compound.getHtmlFormula());
		
		//Mol to volume gas: multiply by molar volume constant
		BigDecimal gas = mol.multiply(molarVolume, threeDigit);
		this.answerKeyString = String.format("%s mol x V<sub>m</sub> = %s mol x %s m<sup>3</sup>/mol = %s m<sup>3</sup>",
				mol.toPlainString(),
				mol.toPlainString(),
				molarVolume.toPlainString(),
				gas.toPlainString());
	}
	
	private void createGasMol (long seed) {
		int factorVolume = 40;
		
		BigDecimal volume = new BigDecimal(seededRandomNums.nextDouble()*factorVolume, threeDigit);
		
		this.questionText = String.format("Bereken hoeveel mol overeenkomt met %s L %s (g) bij 298 K",
				volume.toPlainString(),
				compound.getHtmlFormula());
		
		//volume gas to mol: divide by molar volume constant
		//Attention: this question uses L not m3 as a unit for more sensible mole amounts.
		BigDecimal molarVolLiter = molarVolume.multiply(new BigDecimal(1000), threeDigit);
		BigDecimal mol = volume.divide(molarVolLiter, 3, RoundingMode.HALF_UP);
		this.answerKeyString = String.format("%s m<sup>3</sup>/mol = %s L/mol</br>&nbsp;&nbsp; %s L / %s L/mol = %s mol %s", 
				molarVolume.toPlainString(),
				molarVolLiter.toPlainString(),
				molarVolLiter.toPlainString(),
				volume.toPlainString(),
				mol.toPlainString(),
				compound.getHtmlFormula());
	}
	
	private void createMassVolume (long seed) {
		int factorMass = 800;
		
		//An extra safeguard for compounds without a density
		try {
			double den = compound.getDensity().doubleValue(); //will throw NullPointerException when the density does not exist.
			if (den == 0.0) {
				System.out.println("Encountered a compound with density 0"+compound.getName()+". Question generation aborted.");
				return;
			};
		} catch (NullPointerException e){
			System.out.println("Encountered a compound without density. Question generation aborted.");
			return;
		}
		
		//density is saved in the compound with too many digits. Fewer digits are required for printing the question.
		BigDecimal density = new BigDecimal(compound.getDensity().toString(), threeDigit);
		
		//Mass units and conversion
		BigDecimal massGrams = new BigDecimal(seededRandomNums.nextDouble()*factorMass, threeDigit);
		BigDecimal massConversion = new BigDecimal("1E-3");
		BigDecimal massKilograms = massGrams.multiply(massConversion, threeDigit);
		
		//Question unit selection. Make sure the last unit is ALWAYS the base unit!! 
		//This fact is used when adapting the answer to the unit.
		String[] units = {"mL", "L", "cm<sup>3</sup>", "m<sup>3</sup>"};
		int unitSelector = seededRandomNums.nextInt(units.length);
		String unit = units[unitSelector];
		
		this.questionText = String.format("Bereken het volume in %s van %s gram %s bij 273 K",
				unit,
				massGrams.toPlainString(),
				compound.getHtmlFormula());
		
		//mass to volume: divide by density
		//Attention: all saved densities are kg/m3!
		BigDecimal volume = massKilograms.divide(compound.getDensity(), threeDigit);
		this.answerKeyString = String.format("%s gram = %s kg</br>&nbsp;&nbsp;%s kg / %s kg/m<sup>3</sup> = %s m<sup>3</sup>", 
				massGrams.toPlainString(),
				massKilograms.toPlainString(),
				massKilograms.toPlainString(),
				density.toString().replace("E+","x10<sup>").concat("</sup>"),
				volume.toPlainString());
		
		//Account for other units
		if (unitSelector != units.length-1) {//the final unit in units is the base unit and requires no further work
			BigDecimal unitFactor;
			switch(unit) {
			case "mL":
			case "cm<sup>3</sup>":
				unitFactor = new BigDecimal("1E6");
				break;
			case "L":
				unitFactor = new BigDecimal("1E3");
				break;
			default:
				unitFactor = new BigDecimal("1");
				break;
			}
			
			//concat another line to the answer for unit conversion
			this.answerKeyString = this.answerKeyString.concat(String.format("</br>&nbsp;&nbsp;%s m<sup>3</sup> = %s %s", 
					volume.toPlainString(),
					volume.multiply(unitFactor, threeDigit).toString().replace("E+", "x10<sup>").concat("</sup>"),
					unit));
		}		
	}
	
	public void createVolumeMass (long seed) {
		int factorVolume = 250;
		
		//An extra safeguard for compounds without a density
		try {
			double den = compound.getDensity().doubleValue(); //will throw NullPointerException when the density does not exist.
			if (den == 0.0) {
				System.out.println("Encountered a compound with density 0: "+compound.getName()+". Question generation aborted.");
				return;
			};
		} catch (NullPointerException e){
			System.out.println("Encountered a compound without density. Question generation aborted.");
			return;
		}
		
		//density is saved in the compound with too many digits. Fewer digits are required for printing the question.
		BigDecimal density = new BigDecimal(compound.getDensity().toString(), threeDigit);
		
		BigDecimal volume = new BigDecimal(seededRandomNums.nextDouble()*factorVolume, threeDigit);
		BigDecimal volumeBaseUnit = volume;
		String unitConversion = "";
		
		//Question unit selection. Make sure the last unit is ALWAYS the base unit!
		//This fact is used when adapting the answer to the unit.
		String[] units = {"mL", "L", "cm<sup>3</sup>", "m<sup>3</sup>"};
		int unitSelector = seededRandomNums.nextInt(units.length);
		String unit = units[unitSelector];
		
		this.questionText = String.format("Bereken de massa van %s %s %s bij 273 K",
				volume.toPlainString(),
				unit,
				compound.getHtmlFormula());
		
		//volume to mass: multiply by density
		//Attention! Density is always in kg/m3.
		if (unitSelector != units.length-1) { //aka: unit is not the base unit
			//Determine factor with which to scale the given unit to the base unit
			BigDecimal unitFactor;
			switch(unit) {
			case "mL":
			case "cm<sup>3</sup>":
				unitFactor = new BigDecimal("1E-6");
				break;
			case "L":
				unitFactor = new BigDecimal("1E-3");
				break;
			default:
				unitFactor = new BigDecimal("1");
				break;
			}
			
			volumeBaseUnit = volume.multiply(unitFactor, threeDigit);
			unitConversion = String.format("%s %s = %s m<sup>3</sup>",
					volume.toPlainString(),
					unit,
					volumeBaseUnit.toString());
			unitConversion = unitConversion + "</br>&nbsp;&nbsp;"; //this makes it fit with the next line of the answer
					
		}
		BigDecimal mass = volumeBaseUnit.multiply(compound.getDensity(), threeDigit);
		String baseUnitCalc = String.format("%s m<sup>3</sup> x %s kg/m<sup>3</sup> = %s kg",
				volumeBaseUnit.toString(),
				density.toString().replace("E+", "x10<sup>").concat("</sup>"),
				mass.toString().replace("E+", "x10<sup>").concat("</sup>"));
		
		this.answerKeyString = unitConversion + baseUnitCalc;
	}
	
	

	public String getQuestionText() {
		return questionText;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
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
