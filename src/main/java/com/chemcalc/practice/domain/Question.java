package com.chemcalc.practice.domain;

import java.math.BigDecimal;
import java.util.Random;
import java.math.MathContext;

public class Question {
	
	private String questionText;
	private int compoundId; //corresponds to database id for compound;
	//private Compound compound;
	private BigDecimal molarMass;
	private BigDecimal starterAmount;
	private BigDecimal answerValue;
	private String answerKeyString;
	
	private MathContext threeDigit = new MathContext(3);
	private MathContext fourDigit = new MathContext(4);
	
	public Question() {
		//The no-argument constructor will create a test question
		//This should not be used for anything but debugging
		
		this.questionText = "Dit is een testvraag met default param.\n"
				+ "Hoeveel mol komt overeen met 10.0 gram CH4?";
		this.molarMass = new BigDecimal(16.0);
		this.starterAmount = new BigDecimal(10.0);
		this.answerValue = starterAmount.divide(molarMass);
		this.answerKeyString = "10.0 / 16.0 = 0.625 mol CH4";
	}
	
	public Question(String questiontype, long seed) {
		//Approach: the switch figures out the questionType and calls 
		//the correct method to further construct the question.
		//I split the making of the question into methods instead of
		//the constructor to keep the constructor more concise.
		switch(questiontype) {
		case "MassMol":
			this.createMassMol(seed);
			break;
		case "MolMass":
			this.createMolMass(seed);
			break;
		default:
			System.out.println("Invalid question type in Question(String, long) constructor");
		}
	}
	
	private void createMassMol(long seed) {
		Random randnums = new Random(seed);
		
		this.compoundId = 0; //Database not ready yet -- substitute for a random database ID later
		this.molarMass = new BigDecimal(16.04, fourDigit); //pull from compound when the object is done
		this.starterAmount = new BigDecimal(randnums.nextDouble()*10, threeDigit);
		
		this.questionText = String.format("Bereken hoeveel mol overeenkomt met %s gram %s.", starterAmount.toPlainString(), "CH4");
		
		// mass to moles is calculated by dividing the mass by the molar mass
		this.answerValue = starterAmount.divide(molarMass, threeDigit);
		this.answerKeyString = String.format("%s / %s = %s mol", starterAmount.toPlainString(), molarMass.toPlainString(), answerValue.toPlainString());	
	}
	
	private void createMolMass(long seed) {
		Random randnums = new Random(seed);
		
		this.compoundId = 0; //Database not ready yet -- substitute for a random database ID later
		this.molarMass = new BigDecimal(16.04, fourDigit); //pull from compound when the object is done
		this.starterAmount = new BigDecimal(randnums.nextDouble()*3, threeDigit);
		
		this.questionText = String.format("Bereken hoeveel gram overeenkomt met %s mol %s.", starterAmount.toPlainString(), "CH4");
		
		// moles to mass: multiply moles by molar mass
		this.answerValue = starterAmount.multiply(molarMass, threeDigit);
		this.answerKeyString = String.format("%s x %s = %s gram", starterAmount.toPlainString(), molarMass.toPlainString(), answerValue.toPlainString());
	}

	public String getQuestionText() {
		return questionText;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	public int getCompoundId() {
		return compoundId;
	}

	public void setCompoundId(int compoundId) {
		this.compoundId = compoundId;
	}

	public BigDecimal getMolarMass() {
		return molarMass;
	}

	public void setMolarMass(BigDecimal molarMass) {
		this.molarMass = molarMass;
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
	
	
}
