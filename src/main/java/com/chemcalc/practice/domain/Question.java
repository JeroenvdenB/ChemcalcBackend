package com.chemcalc.practice.domain;

import java.math.BigDecimal;
import java.util.Random;

import org.apache.el.stream.Optional;

import java.math.MathContext;

import com.chemcalc.practice.controller.CompoundService;
import com.chemcalc.practice.domain.Compound;

public class Question {
	
	private Compound compound;			//argument for the Question constructor. Gets set in constructor.
	private Random seededRandomNums;	//argument for the Question constructor. Gets set in constructor.
	private String questionText;		//set in createXX() method. Question type specific.
	private BigDecimal starterAmount;	//set in createXX() method. Question type specific.
	private BigDecimal answerValue;		//set in createXX() method. Question type specific.
	private String answerKeyString;		//set in createXX() method. Question type specific.
	
	private MathContext threeDigit = new MathContext(3);	//set here - valid for all question types
	private MathContext fourDigit = new MathContext(4);		//set here - valid for all question types
	
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

		switch(questiontype) {
		case "MassMol":
			this.createMassMol(seed);
			break;
		case "MolMass":
			this.createMolMass(seed);
			break;
		default:
			System.out.println("Invalid question type in Question(String, long, Compound) constructor");
		}
	}
	
	private void createMassMol(long seed) {
		//Adjust multiplication factor here to finetune the answers range
		int factor = 50;
		
		this.starterAmount = new BigDecimal(seededRandomNums.nextDouble()*factor, threeDigit);
		this.questionText = String.format("Bereken hoeveel mol overeenkomt met %s gram %s.", starterAmount.toPlainString(), compound.getHtmlFormula());
		
		// mass to moles: divide the mass by the molar mass
		this.answerValue = starterAmount.divide(compound.getMolarMass(), threeDigit);
		this.answerKeyString = String.format("%s / %s = %s mol", starterAmount.toPlainString(), compound.getMolarMass().toPlainString(), answerValue.toPlainString());	
	}
	
	private void createMolMass(long seed) {

		this.starterAmount = new BigDecimal(seededRandomNums.nextDouble()*3, threeDigit);
		this.questionText = String.format("Bereken hoeveel gram overeenkomt met %s mol %s.", starterAmount.toPlainString(), compound.getHtmlFormula());
		
		// moles to mass: multiply moles by molar mass
		this.answerValue = starterAmount.multiply(compound.getMolarMass(), threeDigit);
		this.answerKeyString = String.format("%s x %s = %s gram", starterAmount.toPlainString(), compound.getMolarMass().toPlainString(), answerValue.toPlainString());
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
