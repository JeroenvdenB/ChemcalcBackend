package com.chemcalc.practice.domain;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Random;

import com.chemcalc.practice.domain.generators.generateGasMass;
import com.chemcalc.practice.domain.generators.generateMassGas;

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
	public final String lineBreak = "</br>&nbsp;&nbsp;";
	
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
		 * 		Particles to mol (any non-salt)
		 * 		Mol to molarity (any non-metal except water)
		 * 		Molarity to mol (any non-metal except water)
		 * 		Mol salt to molarity ions (any salt) 
		 * 		Molarity of ions to mol salt (any salt)
		 * 		Molarity to volume required (any non-metal, except water)
		 * 		Mol to volume gas at 298 K (any gas)
		 * 		Volume gas to mol at 298 K (any gas)
		 * 		Mass to volume at 273 K (any non-gas with a density)
		 * 		Volume to mass at 273 K (any non-gas with a density)
		 * 
		 * "multi-step" questions:
		 * 		Molarity to mass (any molecular compound except water)
		 * 		Mass to molarity (any molecular compound except water)
		 * 		Molarity of ions to mass of salt (any salt)
		 * 		Mass of salt to molarity ions (any salt)
		 * 		Mass to particles (any compound)
		 * 		Particles to mass (any non-salt)
		 * 		Mass to volume gas at 298 K (any gas) 
		 * 		Volume gas to mass at 298 K (any gas)
		 * 
		 * What questions do not exist?
		 * 		Number of ions to mol or mass of a salt, and equivalent, the number of atoms
		 * 		to mol or mass of a molecule. It would add a lot of work
		 * 		for relatively little educational benefit. These questions are a rarity, even 
		 * 		in the chapters that cover them.
		 * 		
		 * 		Number of particles (ions or molecules) in solution to molarity, and vise versa.
		 * 		This adds yet another avogadro-involved question type. These are less important
		 * 		than other question types that involve multiple steps. Adding these two question 
		 * 		types would add quite some work for little educational value. Especially late 
		 * 		4th year and beyond, calculating number of particles is virtually never asked of students.
		 */
		
		this.compound = compound;
		this.seededRandomNums = new Random(seed);
		
		// random question selection if questionType is "Random"
		// can't use seededRandomNums - messes with reproducibility because the amount of numbers used from the stream
		Random questionSelection = new Random(seed); 
		
		if (questionType.equals("Random")) {
			
			ArrayList<String> questionTypes = new ArrayList<String>();
			questionTypes.add("MolMass");
			questionTypes.add("MassMol");
			questionTypes.add("MolParticles"); // Selects different questions in the switch below based on compound type!
			questionTypes.add("MassParticles"); //Selects different questions in the switch below based on compound type!
				
			if (!compound.getType().equals("metaal") && !compound.getName().equals("water")) {
				//These question types are invalid for metals, in the DB type "metaal"
				//Also invalid specifically for 'water'
				questionTypes.add("MolMolarity"); 
				questionTypes.add("MolarityMol"); 
				questionTypes.add("MolarityVolume");
				if (!compound.getType().equals("zout")) {
					//I know, you can do it for salts too. However, I want to focus on ion molarity for salts.
					//So mass <--> molarity is NOT for salts. Those get their own Mass <--> molarityIons
					//For the sake of practicing the step, mol to molarity is still included for salts.
					//The chemistry book they use does this too iirc.
					questionTypes.add("MolarityMass");
					questionTypes.add("MassMolarity"); 
				}
			}
				
			if (!compound.getType().equals("zout")) {
				questionTypes.add("ParticlesMol");
				questionTypes.add("ParticlesMass");
			}
			
			if (compound.getType().equals("zout")) {
				questionTypes.add("MolMolarityIons");
				questionTypes.add("MolarityIonsMol");
				questionTypes.add("MolarityIonsMass");
				questionTypes.add("MassMolarityIons");
			}
				
			if (compound.getPhase().equals("g")) { //These questions only make sense for gasses at 298 K
				questionTypes.add("MolGas");
				questionTypes.add("GasMol"); 
				questionTypes.add("MassGas");
				questionTypes.add("GasMass");
			} else if (compound.getDensity().intValue() != 0) { 
				//Not a gas means mass to volume makes sense at 273 K. 
				//Not all compounds have a listed density, so check it's not zero.
				questionTypes.add("MassVolume"); 
				questionTypes.add("VolumeMass"); 
			}
				
			int r = questionSelection.nextInt(questionTypes.size());
			questionType = questionTypes.get(r);							
		}
		
		String[] questionContent;
		
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
		case "MolMolarityIons":
			this.createMolMolarityIons(seed);
			break;
		case "MolarityMol":
			this.createMolarityMol(seed);
			break;
		case "MolarityIonsMol":
			this.createMolarityIonsMol(seed);
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
		case "MolarityMass":
			this.createMolarityMass(seed);
			break;
		case "MolarityIonsMass":
			this.createMolarityIonsMass(seed);
			break;
		case "MassMolarity":
			this.createMassMolarity(seed);
			break;
		case "MassMolarityIons":
			this.createMassMolarityIons(seed);
			break;
		case "MassParticles":
			if (compound.getType().equals("metaal")) {
				this.createMassAtomsMetal(seed);
				break;
			} if (compound.getType().equals("zout")) {
				this.createMassAtomsMolecule(seed); //question is so similar these are the same method as molecules
				break;
			} if (compound.getType().equals("moleculair") && questionSelection.nextDouble() < 0.5) {
				this.createMassAtomsMolecule(seed);
				break;
			} else {
				this.createMassMolecules(seed);
				break;
			}
		case "ParticlesMass":
			this.createParticlesMass(seed);
			break;
		case "MassGas":
			questionContent = generateMassGas.createMassGas(seededRandomNums, compound);
			this.questionText = questionContent[0];
			this.answerKeyString = questionContent[1];
			break;
		case "GasMass":
			questionContent = generateGasMass.createGasMass(seededRandomNums, compound);
			this.questionText = questionContent[0];
			this.answerKeyString = questionContent[1];
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

		
		// mass to moles: divide the mass by the molar mass
		BigDecimal mol = mass.divide(compound.getMolarMass(), threeDigit);
		this.answerKeyString = String.format("%s g / %s g/mol = %s mol %s", 
				mass.toPlainString(), 
				compound.getMolarMass().round(fourDigit).toPlainString(), 
				mol.toPlainString(), compound.getHtmlFormula());	
	}
	
	private void createMolMass(long seed) {
		int factor = 3;

		BigDecimal mol = new BigDecimal(seededRandomNums.nextDouble()*factor, threeDigit);
		this.questionText = String.format("Bereken hoeveel gram overeenkomt met %s mol %s", 
				mol.toPlainString(), 
				compound.getHtmlFormula());
		
		// moles to mass: multiply moles by molar mass
		BigDecimal mass = mol.multiply(compound.getMolarMass(), threeDigit);
		this.answerKeyString = String.format("%s mol x %s g/mol = %s gram %s", 
				mol.toPlainString(), 
				compound.getMolarMass().round(fourDigit).toPlainString(), 
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
	
	private void createMolMolarityIons (long seed) {
		int factorMole = 2;
		int factorVolume = 10;
		
		BigDecimal volume = new BigDecimal(seededRandomNums.nextDouble()*factorVolume, threeDigit);
		BigDecimal mol = new BigDecimal(seededRandomNums.nextDouble()*factorMole, threeDigit);
		
		//Grab composition code in the form "X:3, Y:1, Z:1", split, and randomly select an ion
		String composition = compound.getComposition();
		String[] ions = composition.split(",");
		int ionIndex = (int) Math.floor(seededRandomNums.nextDouble()*ions.length);
		String[] atom = ions[ionIndex].trim().split(":");
		String symbol = atom[0];
		BigDecimal coefficient = new BigDecimal(atom[1]);
		
		//Finish the question asking for the selected atom or ion		
		this.questionText = String.format("Bereken [%s] als %s mol %s word opgelost in %s L oplosmiddel.",
				symbol,
				mol.toPlainString(), 
				compound.getHtmlFormula(), 
				volume.toPlainString());
		
		// moles to molarity ion: divide moles by volume and multiply by the coefficient
		BigDecimal molIons = mol.multiply(coefficient, fourDigit);
		BigDecimal molarity = molIons.divide(volume, threeDigit).multiply(coefficient, threeDigit);
		this.answerKeyString = String.format("%s mol %s x %s = %s mol %s-ionen </br>&nbsp;&nbsp; [%s] = %s mol / %s L = %s M", 
				mol.toPlainString(),
				compound.getHtmlFormula(),
				coefficient.toPlainString(),
				molIons.toPlainString(),
				symbol,
				symbol,
				molIons.toPlainString(),
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
	
	private void createMolarityIonsMol (long seed) {
		int factorVolume = 12;
		int factorMolarity = 2;
		
		BigDecimal volume = new BigDecimal(seededRandomNums.nextDouble()*factorVolume, threeDigit);
		BigDecimal molarity = new BigDecimal(seededRandomNums.nextDouble()*factorMolarity, twoDigit);
		
		//Grab composition code in the form "X:3, Y:1, Z:1", split, and randomly select an ion
		String composition = compound.getComposition();
		String[] ions = composition.split(",");
		int ionIndex = (int) Math.floor(seededRandomNums.nextDouble()*ions.length);
		String[] atom = ions[ionIndex].trim().split(":");
		String symbol = atom[0];
		BigDecimal coefficient = new BigDecimal(atom[1]);
		
		//Finish the question given the concentration of a certain ion	
		this.questionText = String.format("Bereken hoeveel mol %s nodig is om %s L oplossing te maken met [%s]=%s M",
				compound.getHtmlFormula(),
				volume.toPlainString(),
				symbol,
				molarity.toPlainString());
		
		//Molarity ions to mol: multiply molarity by volume for mol ions. Then divide by coefficient for mol salt.
		BigDecimal molIons = molarity.multiply(volume, threeDigit);
		BigDecimal molSalt = molIons.divide(coefficient, twoDigit);		
		this.answerKeyString = String.format("%s M x %s L = %s mol %s-ionen </br>&nbsp;&nbsp; %s mol / %s = %s mol %s", 
				molarity.toPlainString(),
				volume.toPlainString(),
				molIons.toPlainString(),
				symbol,
				molIons.toPlainString(),
				coefficient.toPlainString(),
				molSalt.toPlainString(),
				compound.getHtmlFormula());
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
		this.questionText = String.format("Bereken hoeveel %s-%s %s mol %s bevat", 
				symbol,
				(compound.getType().equals("zout")) ? "ionen" : "atomen",
				mol.toPlainString(), 
				compound.getHtmlFormula());
		
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
	
	public void createMolarityMass(long seed) {
		int factorVolume =12;
		double factorMolarity = 1.5;
		
		BigDecimal volume = new BigDecimal(seededRandomNums.nextDouble()*factorVolume, threeDigit);
		BigDecimal molarity = new BigDecimal(seededRandomNums.nextDouble()*factorMolarity, twoDigit);
		
		this.questionText = String.format("Bereken hoeveel gram %s nodig is voor %s L oplossing van %s M", 
				compound.getName(), 
				volume.toPlainString(), 
				molarity.toPlainString());
		
		// Step 1: molarity to mol: multiply molarity by volume
		// Step 2: mol to mass: multiply mol by molar mass
		BigDecimal mol = molarity.multiply(volume, threeDigit);
		BigDecimal mass = mol.multiply(compound.getMolarMass(), twoDigit);
		String step1 = String.format("%s M x %s L = %s mol %s", 
				molarity.toPlainString(), 
				volume.toPlainString(), 
				mol.toPlainString(), 
				compound.getName());
		String step2 = String.format("%s mol x %s g/mol = %s g %s",
				mol.toPlainString(),
				compound.getMolarMass().round(fourDigit).toPlainString(),
				mass.toString().replace("E+", "x10<sup>").concat("</sup>"),
				compound.getName());
		this.answerKeyString = step1 + lineBreak + step2;
	}
	
	public void createMolarityIonsMass (long seed) {
		int factorVolume =12;
		double factorMolarity = 0.9;
		
		BigDecimal volume = new BigDecimal(seededRandomNums.nextDouble()*factorVolume, threeDigit);
		BigDecimal molarity = new BigDecimal(seededRandomNums.nextDouble()*factorMolarity, twoDigit);
		
		//Grab composition code in the form "X:3, Y:1, Z:1", split, and randomly select an ion
		String composition = compound.getComposition();
		String[] ions = composition.split(",");
		int ionIndex = (int) Math.floor(seededRandomNums.nextDouble()*ions.length);
		String[] atom = ions[ionIndex].trim().split(":");
		String symbol = atom[0];
		BigDecimal coefficient = new BigDecimal(atom[1]);
		
		//Finish the question given the concentration of a certain ion	
		this.questionText = String.format("Bereken hoeveel gram %s nodig is om %s L oplossing te maken met [%s]=%s M",
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
		this.answerKeyString = step1 + lineBreak + step2 + lineBreak + step3;
	}
	
	public void createMassMolarity (long seed) {
		int factorVolume =16;
		double factorMass = 500;
		
		BigDecimal volume = new BigDecimal(seededRandomNums.nextDouble()*factorVolume, threeDigit);
		BigDecimal mass = new BigDecimal(seededRandomNums.nextDouble()*factorMass, threeDigit);
		
		// Certain tiny volumes create nonsense answers.
		// This is a simple safe-guard slapped in place to prevent answers like 300 M...
		if (volume.doubleValue() < 0.1)
			volume = volume.add(new BigDecimal(1), threeDigit);
		
		this.questionText = String.format("Bereken de %s van %s g %s in %s L oplosmiddel", 
				(seededRandomNums.nextDouble() < 0.4) ? "concentratie (in mol/L)" : "molariteit", 
				mass.toPlainString(),
				compound.getHtmlFormula(),
				volume.toPlainString());
		
		// Step 1: mass to mol: divide mass by molar mass
		// Step 2: mol to molarity: divide mol by volume
		BigDecimal mol = mass.divide(compound.getMolarMass(), fourDigit);
		BigDecimal molarity = mol.divide(volume, threeDigit);
		String step1 = String.format("%s g / %s g/mol = %s mol", 
				mass.toPlainString(),
				compound.getMolarMass().round(fourDigit).toPlainString(),
				mol.toPlainString());
		String step2 = String.format("%s mol / %s L = %s M",
				mol.toPlainString(),
				volume.toPlainString(),
				molarity.toPlainString());
		this.answerKeyString = step1 + lineBreak + step2;
	}
	
	public void createMassMolarityIons (long seed) {
		int factorVolume =16;
		double factorMass = 400;
		
		BigDecimal volume = new BigDecimal(seededRandomNums.nextDouble()*factorVolume, threeDigit);
		BigDecimal mass = new BigDecimal(seededRandomNums.nextDouble()*factorMass, threeDigit);
		
		// Certain tiny volumes create nonsense answers.
		// This is a simple safe-guard slapped in place to prevent the occasional answer like 100+ M
		if (volume.doubleValue() < 0.1)
			volume = volume.add(new BigDecimal(1), threeDigit);
		
		// Grab composition code in the form "X:3, Y:1, Z:1", split, and randomly select an ion
		String composition = compound.getComposition();
		String[] ions = composition.split(",");
		int ionIndex = (int) Math.floor(seededRandomNums.nextDouble()*ions.length);
		String[] atom = ions[ionIndex].trim().split(":");
		String symbol = atom[0];
		BigDecimal coefficient = new BigDecimal(atom[1]);
		
		this.questionText = String.format("Bereken [%s] als %s g %s word opgelost in %s L oplosmiddel", 
				symbol,
				mass.toPlainString(),
				compound.getHtmlFormula(),
				volume.toPlainString());
		
		// Step 1: mass to mol: divide mass by molar mass
		// Step 2: mol salt to mol ions: multiply mol salt by coefficient
		// Step 3: mol ions to molarity: divide mol ions by volume
		BigDecimal molSalt = mass.divide(compound.getMolarMass(), fourDigit) ;
		BigDecimal molIons = molSalt.multiply(coefficient, fourDigit);
		BigDecimal molarity = molIons.divide(volume, threeDigit);
		String step1 = String.format("%s g / %s g/mol = %s mol %s", 
				mass.toPlainString(),
				compound.getMolarMass().round(fourDigit).toPlainString(),
				molSalt.toPlainString(),
				compound.getHtmlFormula());
		String step2 = String.format("%s mol x %s = %s mol %s", 
				molSalt.toPlainString(),
				coefficient.toPlainString(),
				molIons.toPlainString(),
				symbol);
		String step3 = String.format("[%s] = %s mol / %s L = %s M", 
				symbol,
				molIons.toPlainString(),
				volume.toPlainString(),
				molarity.toPlainString());
		this.answerKeyString = step1 + lineBreak + step2 + lineBreak + step3;
	}
	
	private void createMassAtomsMetal (long seed) {
		int factorMol = 3;
		
		BigDecimal mol = new BigDecimal(seededRandomNums.nextDouble()*factorMol, fourDigit);
		BigDecimal mass = mol.multiply(compound.getMolarMass(), threeDigit);
		BigDecimal numberAtoms = mol.multiply(avogadro, threeDigit);
		
		this.questionText = String.format("Bereken hoeveel %s-atomen %s gram %s bevat", 
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
		
		this.answerKeyString = step1 + lineBreak + step2;
	}
	
	public void createMassAtomsMolecule (long seed) {
		double factorMol = 1.3;
		
		BigDecimal mol = new BigDecimal(seededRandomNums.nextDouble()*factorMol, fourDigit);
		BigDecimal mass = mol.multiply(compound.getMolarMass(), threeDigit);
		
		//Grab composition code in the form "X:3, Y:1, Z:1", split, and randomly select an atom or ion
		String composition = compound.getComposition();
		String[] atoms = composition.split(",");
		int atomIndex = (int) Math.floor(seededRandomNums.nextDouble()*atoms.length);
		String[] atom = atoms[atomIndex].trim().split(":");
		String symbol = atom[0];
		BigDecimal coefficient = new BigDecimal(atom[1]);
		
		//Finish the question asking for the selected atom or ion
		this.questionText = String.format("Bereken hoeveel %s-atomen %s gram %s bevat", 
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
			
		this.answerKeyString = step1 + lineBreak + step2 + lineBreak + step3;
			
		if (compound.getType().equals("zout")) {
			this.questionText = this.questionText.replace("atomen", "ionen");
			this.answerKeyString = this.answerKeyString.replace("atomen", "ionen");
		}
	}
	
	public void createMassMolecules (long seed) {
		int factorMol = 4;
		
		BigDecimal mol = new BigDecimal(seededRandomNums.nextDouble()*factorMol, fourDigit);
		BigDecimal mass = mol.multiply(compound.getMolarMass(), threeDigit);
		BigDecimal molecules = mol.multiply(avogadro, threeDigit);
		
		this.questionText = String.format("Bereken hoeveel %s-moleculen %s gram %s bevat", 
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
		
		this.answerKeyString = step1 + lineBreak + step2;
	}
	
	public void createParticlesMass (long seed) {
		int factorMol = 3;
		
		BigDecimal mol = new BigDecimal(seededRandomNums.nextDouble()*factorMol, fourDigit);
		BigDecimal mass = mol.multiply(compound.getMolarMass(), threeDigit);
		BigDecimal particles = mol.multiply(avogadro, threeDigit);
		
		this.questionText = String.format("Bereken de massa van %s %s-moleculen", //replace to "atomen" later if molecular compound
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
		
		this.answerKeyString = step1 + lineBreak + step2;
	
		// To check, grab the composition, split it, and check the coefficient. There should be only one
		// component, and the coefficient is 1 for all metals and mono-atomic molecular compounds.
		// If it is mono-atomic or a metal, 'molecules' should change to 'atoms'.
		String[] components = compound.getComposition().split(",");
		char coefficient = components[0].charAt(components[0].length()-1); // This does not account for double-digit coefficients. 
		if (components.length == 1 && coefficient == '1') {
			this.questionText = this.questionText.replace("moleculen", "atomen");
			this.answerKeyString = this.answerKeyString.replace("moleculen", "atomen");
		}		
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
