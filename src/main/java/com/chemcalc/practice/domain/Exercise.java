package com.chemcalc.practice.domain;

import java.util.ArrayList;
import java.util.Random;

public class Exercise {
	
	private String questionType;
	private int repetitions;
	private long seed;
	private ArrayList<Question> questionsList = new ArrayList<Question>();
	
	public Exercise () {
		//Default for debugging
		//Do not accept non-argument constructors in deployment!
		this.questionType = "MassMol";
		this.repetitions = 1;
		this.seed = 0;
		this.createQuestions();
	}
	
	public Exercise (String questionType, int repetitions) {
		//If no seed is given, create a random one.
		//It's used in too many places in Question's methods to
		//handle the absence of a seed for every single method.
		//No seed just means all values are random anyway, without
		//the need for reproducibility. Creating one 'master seed'
		//is fine.
		
		//Handle question types here, because it can be multiple
		//But the question object can only be one type per question!
		this.questionType = questionType;
		this.repetitions = repetitions;
		this.seed = new Random().nextLong();
		this.createQuestions();
	}
	
	public Exercise (String questionType, int repetitions, long seed) {
		//TODO should be the same as the above, only without the random seed
		this.questionType = questionType;
		this.repetitions = repetitions;
		this.seed = seed;
		this.createQuestions();
	}

	private void createQuestions() {
		//Each question gets a random subseed, because the questions need to be random...
		//... yet repeatable if needed! 
		//The subseeds, however, should always be the same if a master seed is given.
		//This will lead to identical subseeds, and identical questions, for a given master seed.
		
		Random subRandom = new Random(seed);
		for (int i=0; i<repetitions; i++) {
			long subseed = subRandom.nextLong();
			Question q = new Question(questionType, subseed);
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
