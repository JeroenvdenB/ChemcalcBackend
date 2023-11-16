package com.chemcalc.practice.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.chemcalc.practice.domain.generators.GenerateGasMass;
import com.chemcalc.practice.domain.generators.GenerateMassGas;
import com.chemcalc.practice.domain.generators.GenerateMassMolarity;
import com.chemcalc.practice.domain.generators.GenerateMassMolarityIons;
import com.chemcalc.practice.domain.generators.GenerateMassParticles;
import com.chemcalc.practice.domain.generators.GenerateMassVolume;
import com.chemcalc.practice.domain.generators.GenerateMolarityIonsMass;
import com.chemcalc.practice.domain.generators.GenerateMolarityMass;
import com.chemcalc.practice.domain.generators.GenerateParticlesMass;
import com.chemcalc.practice.domain.generators.GenerateVolumeMass;

public class Exercise {
	
	private String exerciseType;
	private int repetitions;
	private long seed;
	private ArrayList<Question> questionsList = new ArrayList<Question>();
	
	public Exercise (String exerciseType, int repetitions, long seed) {
		this.exerciseType = exerciseType;
		this.repetitions = repetitions;
		this.seed = seed;
	}

	public void fillQuestionsList(List<Compound> compounds) {
		// Set a subseed, so that each question gets a unique seed from this Random object
		// Keeping the source (this Random object) seeded ensures reproducibility.
		Random questionSeeds = new Random(seed);
		
		for (Compound compound: compounds) {
			long subseed = questionSeeds.nextLong();
			String questionType = exerciseType;
			
			// If question type is random, determine what the type should be
			if (exerciseType.equals("Random")) {
				questionType = chooseQuestionType(subseed, compound);
				System.out.println("Random question detected");
			}
			
			Question question = createQuestion(questionType, subseed, compound);
			questionsList.add(question);
		}
	}
	
	public Question createQuestion(String questionType, long subseed, Compound compound) {
		
		Question question = new Question("Oeps, er ging iets verkeerd...", "Geen antwoord gegenereerd");
		
		switch(questionType) {
//		case "MassMol":
//			this.createMassMol(seed);
//			break;
//		case "MolMass":
//			this.createMolMass(seed);
//			break;
//		case "MolMolarity":
//			this.createMolMolarity(seed);
//			break;
//		case "MolMolarityIons":
//			this.createMolMolarityIons(seed);
//			break;
//		case "MolarityMol":
//			this.createMolarityMol(seed);
//			break;
//		case "MolarityIonsMol":
//			this.createMolarityIonsMol(seed);
//			break;
//		case "MolarityVolume":
//			this.createMolarityVolume(seed);
//			break;
//		case "MolParticles":
//			if (compound.getType().equals("metaal")) {
//				this.createMolAtomsMetal(seed);
//				break;
//			} if (compound.getType().equals("zout")) {
//				this.createMolAtomsMolecule(seed); //question is so similar these are the same method as molecules
//				break;
//			} if (compound.getType().equals("moleculair") && questionSelection.nextDouble() < 0.5) {
//				this.createMolAtomsMolecule(seed);
//				break;
//			} else {
//				this.createMolMolecules(seed);
//				break;
//			}
//		case "ParticlesMol":
//			this.createParticlesMol(seed);
//			break;
//		case "MolGas":
//			this.createMolGas(seed);
//			break;
//		case "GasMol":
//			this.createGasMol(seed);
//			break;
		case "MassVolume":
			question = GenerateMassVolume.create(subseed, compound);
			break;
		case "VolumeMass":
			question = GenerateVolumeMass.create(subseed, compound);
			break;
		case "MolarityMass":
			question = GenerateMolarityMass.create(subseed, compound);
			break;
		case "MolarityIonsMass":
			question = GenerateMolarityIonsMass.create(subseed, compound);
			break;
		case "MassMolarity":
			question = GenerateMassMolarity.create(subseed, compound);
			break;
		case "MassMolarityIons":
			question = GenerateMassMolarityIons.create(subseed, compound);
			break;
		case "MassParticles":
			question = GenerateMassParticles.create(subseed, compound);
			break;
		case "ParticlesMass":
			question = GenerateParticlesMass.create(subseed, compound);
			break;
		case "MassGas":
			question = GenerateMassGas.create(subseed, compound);
			break;
		case "GasMass":
			question = GenerateGasMass.create(subseed, compound);
			break;
		default:
			System.out.println("Invalid question type in Exercise -> createQuestion method.");
		}
	
		return question;
	}
	
	public String chooseQuestionType(long subseed, Compound compound) {
		//Determine and collect all valid question types for the compound
		ArrayList<String> questionTypes = new ArrayList<String>();
		questionTypes.add("MolMass");
		questionTypes.add("MassMol");
		// Basic questions with 'particles' are subdivided later
		// Selecting sub-types here would skew the random selection
		questionTypes.add("MolParticles"); 
		questionTypes.add("MassParticles");
			
		if (!compound.getType().equals("metaal") && !compound.getName().equals("water")) {
			//These question types are invalid for metals, in the DB type "metaal"
			//Also invalid specifically for 'water'
			questionTypes.add("MolMolarity"); 
			questionTypes.add("MolarityMol"); 
			questionTypes.add("MolarityVolume");
			if (!compound.getType().equals("zout")) {
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
			
		if (compound.getPhase().equals("g")) { //These questions only make sense for gases at 298 K
			questionTypes.add("MolGas");
			questionTypes.add("GasMol"); 
			questionTypes.add("MassGas");
			questionTypes.add("GasMass");
		} else if (compound.getDensity().intValue() != 0) { 
			//Not all compounds have a listed density, so check it's not zero.
			questionTypes.add("MassVolume"); 
			questionTypes.add("VolumeMass"); 
		}
		
		Random questionSelector = new Random(subseed);
		int j = questionSelector.nextInt(questionTypes.size());
		
		return questionTypes.get(j);
	}

	public String getExerciseType() {
		return exerciseType;
	}

	public void setExerciseType(String exerciseType) {
		this.exerciseType = exerciseType;
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