package com.chemcalc.practice.domain;

public class Question {
	
	private String questionText;		//set in createXX() method. Question type specific.
	private String answerKeyString;		//set in createXX() method. Question type specific.
	
	public Question(String text, String answer) {
		this.questionText = text;
		this.answerKeyString = answer;
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
	
	
}
	
//	
//	private void createMassMol(long seed) {
//		//Adjust multiplication factor here to finetune the answers range
//		int factorMass = 80;
//		
//		BigDecimal mass = new BigDecimal(seededRandomNums.nextDouble()*factorMass, threeDigit);
//		this.questionText = String.format("Bereken hoeveel mol overeenkomt met %s gram %s", 
//				mass.toPlainString(), 
//				compound.getHtmlFormula());
//
//		
//		// mass to moles: divide the mass by the molar mass
//		BigDecimal mol = mass.divide(compound.getMolarMass(), threeDigit);
//		this.answerKeyString = String.format("%s g / %s g/mol = %s mol %s", 
//				mass.toPlainString(), 
//				compound.getMolarMass().round(fourDigit).toPlainString(), 
//				mol.toPlainString(), compound.getHtmlFormula());	
//	}
//	
//	private void createMolMass(long seed) {
//		int factor = 3;
//
//		BigDecimal mol = new BigDecimal(seededRandomNums.nextDouble()*factor, threeDigit);
//		this.questionText = String.format("Bereken hoeveel gram overeenkomt met %s mol %s", 
//				mol.toPlainString(), 
//				compound.getHtmlFormula());
//		
//		// moles to mass: multiply moles by molar mass
//		BigDecimal mass = mol.multiply(compound.getMolarMass(), threeDigit);
//		this.answerKeyString = String.format("%s mol x %s g/mol = %s gram %s", 
//				mol.toPlainString(), 
//				compound.getMolarMass().round(fourDigit).toPlainString(), 
//				mass.toPlainString(), compound.getHtmlFormula());
//	}
//	
//	private void createMolMolarity(long seed) {
//		int factorMole = 2;
//		int factorVolume = 12;
//		
//		BigDecimal volume = new BigDecimal(seededRandomNums.nextDouble()*factorVolume, threeDigit);
//		BigDecimal mol = new BigDecimal(seededRandomNums.nextDouble()*factorMole, threeDigit);
//		
//		this.questionText = String.format("Bereken de molariteit van een oplossing van %s mol %s in %s L oplosmiddel", 
//				mol.toPlainString(), 
//				compound.getName(), 
//				volume.toPlainString());
//		
//		// moles to molarity: divide moles by volume
//		BigDecimal molarity = mol.divide(volume, threeDigit);
//		this.answerKeyString = String.format("%s mol / %s L = %s M", 
//				mol.toPlainString(), 
//				volume.toPlainString(), 
//				molarity.toPlainString());
//	}
//	
//	private void createMolMolarityIons (long seed) {
//		int factorMole = 2;
//		int factorVolume = 10;
//		
//		BigDecimal volume = new BigDecimal(seededRandomNums.nextDouble()*factorVolume, threeDigit);
//		BigDecimal mol = new BigDecimal(seededRandomNums.nextDouble()*factorMole, threeDigit);
//		
//		//Grab composition code in the form "X:3, Y:1, Z:1", split, and randomly select an ion
//		String composition = compound.getComposition();
//		String[] ions = composition.split(",");
//		int ionIndex = (int) Math.floor(seededRandomNums.nextDouble()*ions.length);
//		String[] atom = ions[ionIndex].trim().split(":");
//		String symbol = atom[0];
//		BigDecimal coefficient = new BigDecimal(atom[1]);
//		
//		//Finish the question asking for the selected atom or ion		
//		this.questionText = String.format("Bereken [%s] als %s mol %s word opgelost in %s L oplosmiddel.",
//				symbol,
//				mol.toPlainString(), 
//				compound.getHtmlFormula(), 
//				volume.toPlainString());
//		
//		// moles to molarity ion: divide moles by volume and multiply by the coefficient
//		BigDecimal molIons = mol.multiply(coefficient, fourDigit);
//		BigDecimal molarity = molIons.divide(volume, threeDigit).multiply(coefficient, threeDigit);
//		this.answerKeyString = String.format("%s mol %s x %s = %s mol %s-ionen </br>&nbsp;&nbsp; [%s] = %s mol / %s L = %s M", 
//				mol.toPlainString(),
//				compound.getHtmlFormula(),
//				coefficient.toPlainString(),
//				molIons.toPlainString(),
//				symbol,
//				symbol,
//				molIons.toPlainString(),
//				volume.toPlainString(),
//				molarity.toPlainString());
//	}
//	
//	private void createMolarityMol(long seed) {
//		int factorVolume =12;
//		double factorMolarity = 1.5;
//		
//		BigDecimal volume = new BigDecimal(seededRandomNums.nextDouble()*factorVolume, threeDigit);
//		BigDecimal molarity = new BigDecimal(seededRandomNums.nextDouble()*factorMolarity, twoDigit);
//		
//		this.questionText = String.format("Bereken hoeveel mol %s nodig is voor %s L oplossing van %s M", 
//				compound.getName(), 
//				volume.toPlainString(), 
//				molarity.toPlainString());
//		
//		// molarity to moles: multiply molarity by volume
//		BigDecimal mol = molarity.multiply(volume, twoDigit);
//		this.answerKeyString = String.format("%s M x %s L = %s mol %s", 
//				molarity.toPlainString(), 
//				volume.toPlainString(), 
//				mol.toPlainString(), 
//				compound.getName());
//	}
//	
//	private void createMolarityIonsMol (long seed) {
//		int factorVolume = 12;
//		int factorMolarity = 2;
//		
//		BigDecimal volume = new BigDecimal(seededRandomNums.nextDouble()*factorVolume, threeDigit);
//		BigDecimal molarity = new BigDecimal(seededRandomNums.nextDouble()*factorMolarity, twoDigit);
//		
//		//Grab composition code in the form "X:3, Y:1, Z:1", split, and randomly select an ion
//		String composition = compound.getComposition();
//		String[] ions = composition.split(",");
//		int ionIndex = (int) Math.floor(seededRandomNums.nextDouble()*ions.length);
//		String[] atom = ions[ionIndex].trim().split(":");
//		String symbol = atom[0];
//		BigDecimal coefficient = new BigDecimal(atom[1]);
//		
//		//Finish the question given the concentration of a certain ion	
//		this.questionText = String.format("Bereken hoeveel mol %s nodig is om %s L oplossing te maken met [%s]=%s M",
//				compound.getHtmlFormula(),
//				volume.toPlainString(),
//				symbol,
//				molarity.toPlainString());
//		
//		//Molarity ions to mol: multiply molarity by volume for mol ions. Then divide by coefficient for mol salt.
//		BigDecimal molIons = molarity.multiply(volume, threeDigit);
//		BigDecimal molSalt = molIons.divide(coefficient, twoDigit);		
//		this.answerKeyString = String.format("%s M x %s L = %s mol %s-ionen </br>&nbsp;&nbsp; %s mol / %s = %s mol %s", 
//				molarity.toPlainString(),
//				volume.toPlainString(),
//				molIons.toPlainString(),
//				symbol,
//				molIons.toPlainString(),
//				coefficient.toPlainString(),
//				molSalt.toPlainString(),
//				compound.getHtmlFormula());
//	}
//	
//	private void createMolarityVolume(long seed) {
//		int factorMolarity = 2;
//		double factorMol = 1.5;
//		
//		BigDecimal molarity = new BigDecimal(seededRandomNums.nextDouble()*factorMolarity, threeDigit);
//		BigDecimal mol = new BigDecimal(seededRandomNums.nextDouble()*factorMol, threeDigit);
//		this.questionText = String.format("Bereken hoeveel oplosmiddel nodig is om van %s mol %s een %s M oplossing te maken",
//					mol.toPlainString(), compound.getName(), molarity.toPlainString());
//		
//		//volume required: moles divided by molarity
//		BigDecimal volume = mol.divide(molarity, threeDigit);
//		this.answerKeyString = String.format("%s mol / %s M = %s L oplosmiddel", 
//				mol.toPlainString(), 
//				molarity.toPlainString(), 
//				volume.toPlainString());
//	}
//	
//	private void createMolMolecules(long seed) {
//		int factorMol = 2;
//
//		BigDecimal mol = new BigDecimal(seededRandomNums.nextDouble()*factorMol, threeDigit);
//		this.questionText = String.format("Bereken hoeveel %s-moleculen %s mol %s bevat", 
//				compound.getHtmlFormula(), 
//				mol.toPlainString(), 
//				compound.getName());
//			
//		//mol to particles: multiply mol by avogadro
//		BigDecimal molecules = mol.multiply(avogadro, threeDigit);
//		this.answerKeyString = String.format("%s mol x %s = %s %s-moleculen", 
//				mol.toPlainString(), 
//				avogadro.toString().replace("E+23", "x10<sup>23</sup>"),
//				molecules.toString().replace("E+", "x10<sup>").concat("</sup>"), 
//				compound.getHtmlFormula());
//	}
//	
//	private void createMolAtomsMolecule(long seed) {
//		double factorMol = 1.5;
//		
//		BigDecimal mol = new BigDecimal(seededRandomNums.nextDouble()*factorMol, threeDigit);
//		
//		//Grab composition code in the form "X:3, Y:1, Z:1", split, and randomly select an atom or ion
//		String composition = compound.getComposition();
//		String[] atoms = composition.split(",");
//		int atomIndex = (int) Math.floor(seededRandomNums.nextDouble()*atoms.length);
//		String[] atom = atoms[atomIndex].trim().split(":");
//		String symbol = atom[0];
//		BigDecimal coefficient = new BigDecimal(atom[1]);
//		
//		//Finish the question asking for the selected atom or ion
//		this.questionText = String.format("Bereken hoeveel %s-%s %s mol %s bevat", 
//				symbol,
//				(compound.getType().equals("zout")) ? "ionen" : "atomen",
//				mol.toPlainString(), 
//				compound.getHtmlFormula());
//		
//		//mol to atoms: multiply by avogadro and the atom or ion coefficient
//		BigDecimal molecules = mol.multiply(avogadro, fourDigit);
//		BigDecimal molIons = mol.multiply(coefficient, threeDigit);
//		BigDecimal numberAtoms = molecules.multiply(coefficient, threeDigit);
//		
//		if (compound.getType().equals("moleculair")) {
//			this.answerKeyString = String.format("%s mol x %s = %s moleculen.</br>&nbsp;&nbsp;%s moleculen x %s = %s %s-atomen", 
//					mol.toPlainString(), 
//					avogadro.toString().replace("E+23", "x10<sup>23</sup>"), 
//					molecules.toString().replace("E+", "x10<sup>").concat("</sup>"), 
//					molecules.toString().replace("E+", "x10<sup>").concat("</sup>"),
//					coefficient.toPlainString(),
//					numberAtoms.toString().replace("E+", "x10<sup>").concat("</sup>"),
//					symbol);
//		} if (compound.getType().equals("zout")) {
//			this.answerKeyString = String.format("%s mol x %s = %s mol %s-ionen</br>&nbsp;&nbsp;%s mol x %s = %s %s-ionen", 
//					mol.toPlainString(),
//					coefficient.toPlainString(),
//					molIons.toPlainString(),
//					symbol,
//					molIons.toPlainString(),
//					avogadro.toString().replace("E+23", "x10<sup>23</sup>"),
//					numberAtoms.toString().replace("E+", "x10<sup>").concat("</sup>"),
//					symbol);
//		}
//	}
//	
//	private void createMolAtomsMetal (long seed) {
//		int factorMol = 3;
//		
//		BigDecimal mol = new BigDecimal(seededRandomNums.nextDouble()*factorMol, threeDigit);
//		
//		this.questionText = String.format("Bereken hoeveel %s-atomen %s mol %s bevat", 
//				compound.getHtmlFormula(), 
//				mol.toPlainString(), 
//				compound.getName());
//		
//		//mol to atoms: multiply by avogadro
//		BigDecimal numberAtoms = mol.multiply(avogadro, threeDigit);
//		this.answerKeyString = String.format("%s mol x %s = %s %s-atomen",
//				mol.toPlainString(), 
//				avogadro.toString().replace("E+23", "x10<sup>23</sup>"),
//				numberAtoms.toString().replace("E+", "x10<sup>").concat("</sup>"),
//				compound.getHtmlFormula());
//	}
//	
//	private void createParticlesMol (long seed) {
//		int factorMol = 4;
//		
//		//Add some variance in digits
//		MathContext[] accuracies = {twoDigit, threeDigit, fourDigit};
//		int variance = seededRandomNums.nextInt(accuracies.length);
//		
//		BigDecimal mole = new BigDecimal(seededRandomNums.nextDouble()*factorMol, accuracies[variance]);
//		BigDecimal particles = mole.multiply(avogadro, accuracies[variance]);
//		
//		this.questionText = String.format("Bereken hoeveel mol %s overeenkomt met %s %s-%s",
//				compound.getHtmlFormula(), 
//				particles.toString().replace("E+", "x10<sup>").concat("</sup>"),
//				compound.getHtmlFormula(),
//				compound.getType().equals("moleculair")?"moleculen":"atomen");
//		
//		//particles to mol: divide by avogadro, but that value is already present as 'mole'
//		this.answerKeyString = String.format("%s %s / %s = %s mol %s",
//				particles.toString().replace("E+", "x10<sup>").concat("</sup>"),
//				compound.getType().equals("moleculair")?"moleculen":"atomen",
//				avogadro.toString().replace("E+23", "x10<sup>23</sup>"),
//				mole.toPlainString(),
//				compound.getHtmlFormula());
//	}
//	
//	private void createMolGas (long seed) {
//		int factorMol = 5;
//		
//		BigDecimal mol = new BigDecimal(seededRandomNums.nextDouble()*factorMol, threeDigit);
//		
//		this.questionText = String.format("Bereken hoeveel m<sup>3</sup> overeenkomt met %s mol %s (g) bij 298 K",
//				mol.toPlainString(), compound.getHtmlFormula());
//		
//		//Mol to volume gas: multiply by molar volume constant
//		BigDecimal gas = mol.multiply(molarVolume, threeDigit);
//		this.answerKeyString = String.format("%s mol x V<sub>m</sub> = %s mol x %s m<sup>3</sup>/mol = %s m<sup>3</sup>",
//				mol.toPlainString(),
//				mol.toPlainString(),
//				molarVolume.toPlainString(),
//				gas.toPlainString());
//	}
//	
//	private void createGasMol (long seed) {
//		int factorVolume = 40;
//		
//		BigDecimal volume = new BigDecimal(seededRandomNums.nextDouble()*factorVolume, threeDigit);
//		
//		this.questionText = String.format("Bereken hoeveel mol overeenkomt met %s L %s (g) bij 298 K",
//				volume.toPlainString(),
//				compound.getHtmlFormula());
//		
//		//volume gas to mol: divide by molar volume constant
//		//Attention: this question uses L not m3 as a unit for more sensible mole amounts.
//		BigDecimal molarVolLiter = molarVolume.multiply(new BigDecimal(1000), threeDigit);
//		BigDecimal mol = volume.divide(molarVolLiter, 3, RoundingMode.HALF_UP);
//		this.answerKeyString = String.format("%s m<sup>3</sup>/mol = %s L/mol</br>&nbsp;&nbsp; %s L / %s L/mol = %s mol %s", 
//				molarVolume.toPlainString(),
//				molarVolLiter.toPlainString(),
//				molarVolLiter.toPlainString(),
//				volume.toPlainString(),
//				mol.toPlainString(),
//				compound.getHtmlFormula());
//	}
//	


