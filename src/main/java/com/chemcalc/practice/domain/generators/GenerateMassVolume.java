package com.chemcalc.practice.domain.generators;

import java.math.BigDecimal;
import java.util.Random;

import com.chemcalc.practice.domain.Compound;
import com.chemcalc.practice.domain.Question;

public class GenerateMassVolume implements Constants {
	private GenerateMassVolume() {};
	
	static public Question create(long subseed, Compound compound) {
		Random localRandomNums = new Random(subseed);
		int factorMass = 800;
		
		//An extra safeguard for compounds without a density
		try {
			double den = compound.getDensity().doubleValue(); //will throw NullPointerException when the density does not exist.
			if (den == 0.0) {
				System.out.println("Encountered a compound with density 0"+compound.getName()+". Question generation aborted.");
				return new Question("Error: density 0 in database", "Error: density 0 in database");
			};
		} catch (NullPointerException e){
			System.out.println("Encountered a compound without density. Question generation aborted.");
			return new Question("Error: no density in database", "Error: no density in database");
		}
		
		//density is saved in the compound with too many digits. Fewer digits are required for printing the question.
		BigDecimal density = new BigDecimal(compound.getDensity().toString(), threeDigit);
		
		//Mass units and conversion
		BigDecimal massGrams = new BigDecimal(localRandomNums.nextDouble()*factorMass, threeDigit);
		BigDecimal massConversion = new BigDecimal("1E-3");
		BigDecimal massKilograms = massGrams.multiply(massConversion, threeDigit);
		
		//Question unit selection. Make sure the last unit is ALWAYS the base unit!! 
		//This fact is used when adapting the answer to the unit.
		String[] units = {"mL", "L", "cm<sup>3</sup>", "m<sup>3</sup>"};
		int unitSelector = localRandomNums.nextInt(units.length);
		String unit = units[unitSelector];
		
		String text = String.format("Bereken het volume in %s van %s gram %s bij 273 K",
				unit,
				massGrams.toPlainString(),
				compound.getHtmlFormula());
		
		// Step 1: convert unit
		// Step 2: mass to volume: divide by density
		BigDecimal volume = massKilograms.divide(compound.getDensity(), threeDigit);
		String step1 = String.format("%s gram = %s kg", 
				massGrams.toPlainString(),
				massKilograms.toPlainString());
		String step2 = String.format("%s kg / %s kg/m<sup>3</sup> = %s m<sup>3</sup>",
				massKilograms.toPlainString(),
				density.toString().replace("E+","x10<sup>").concat("</sup>"),
				volume.toPlainString());
		
		String answer = step1 + lineBreak + step2;
		
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
			
			// Add another line to the answer for unit conversion
			String step3 = String.format("%s m<sup>3</sup> = %s %s", 
					volume.toPlainString(),
					volume.multiply(unitFactor, threeDigit).toString().replace("E+", "x10<sup>").concat("</sup>"),
					unit);
			
			answer = answer.concat(lineBreak).concat(step3);
		}		
	
		return new Question(text, answer);
	}
	

}
