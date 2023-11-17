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






