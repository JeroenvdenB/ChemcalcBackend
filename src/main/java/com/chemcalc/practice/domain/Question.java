package com.chemcalc.practice.domain;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Random;

public class Question {
	
	private Compound compound;			//argument for the Question constructor. Gets set in constructor.
	private Random seededRandomNums;	//argument for the Question constructor. Gets set in constructor.
	private String questionText;		//set in createXX() method. Question type specific.
	private BigDecimal starterAmount;	//set in createXX() method. Question type specific.
	private BigDecimal answerValue;		//set in createXX() method. Question type specific.
	private String answerKeyString;		//set in createXX() method. Question type specific.
	
	private MathContext fourDigit = new MathContext(4);		//set here - valid for all question types
	private MathContext threeDigit = new MathContext(3);	//set here - valid for all question types
	private MathContext twoDigit = new MathContext(2); 		//set here - valid for all question types
	
	public Question() {
		//The no-argument constructor will create a test question
		//This should not be used for anything but debugging
		
		this.questionText = "Dit is een testvraag met default param.\n"
				+ "Hoeveel mol komt overeen met 10.0 gram CH4?";
		BigDecimal molarMass = new BigDecimal(16.0);
		this.starterAmount = new BigDecimal(10.0);
		this.answerValue = starterAmount.divide(molarMass);
		this.answerKeyString = "10.0 / 16.0 = 0.625 mol CH4";
	}
	
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
		
		//Select which question type is generated randomly for questiontype "Random"
		Random questionSelection = new Random(seed); //can't use seededRandomNums - messes with reproducibility because the amount of numbers used from the stream in question selection varies
		if (questiontype.equals("Random")) {
			ArrayList<String> questionTypes = new ArrayList<String>();
			questionTypes.add("MolMass");
			questionTypes.add("MassMol");
			
			if (!compound.getType().equals("metaal") && !compound.getName().equals("water")) {
				//These question types are invalid for metals, in the DB type "metaal"
				//Also invalid specifically for 'water'
				questionTypes.add("MolMolarity"); 
				questionTypes.add("MolarityMol"); 
				questionTypes.add("MolarityVolume");
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
				answerValue.toPlainString(), compound.getName());	
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
				answerValue.toPlainString(), compound.getName());
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
