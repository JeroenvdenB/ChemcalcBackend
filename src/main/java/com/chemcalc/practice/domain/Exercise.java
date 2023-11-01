package com.chemcalc.practice.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Exercise {
	
	private String questionType;
	private int repetitions;
	private long seed;
	private ArrayList<Question> questionsList = new ArrayList<Question>();
	
	public Exercise (String questionType, int repetitions, long seed) {
		this.questionType = questionType;
		this.repetitions = repetitions;
		this.seed = seed;
	}

	public void createQuestions(List<Compound> compounds) {
		Random subSeeds = new Random(seed);
		for(Compound compound : compounds) {
			long subseed = subSeeds.nextLong();
			Question q = new Question(questionType, subseed, compound);
			questionsList.add(q);
		}
	}
	
	public String getQuestionType() {
		return questionType;
	}

	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}

	public int getRepetitions() {
		return repetitions;
	}

	public void setRepetitions(int repetitions) {
		this.repetitions = repetitions;
	}

	public long getSeed() {
		return seed;
	}

	public void setSeed(long seed) {
		this.seed = seed;
	}

	public ArrayList<Question> getQuestionsList() {
		return questionsList;
	}

	public void setQuestionsList(ArrayList<Question> questionsList) {
		this.questionsList = questionsList;
	}

}
