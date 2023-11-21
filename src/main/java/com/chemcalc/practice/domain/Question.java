package com.chemcalc.practice.domain;

public class Question {
	
	private String questionText;
	private String answerKeyString;		
	
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
	