package com.chemcalc.practice.domain.generators;

import java.math.BigDecimal;
import java.util.Random;

import com.chemcalc.practice.domain.Compound;
import com.chemcalc.practice.domain.Question;

public class GenerateVolumeMass implements Constants {
	private GenerateVolumeMass() {};

	static public Question create(long subseed, Compound compound) {
		Random localRandomNums = new Random(subseed);
		int factorVolume = 250;

		//An extra safeguard for compounds without a density
		try {
			double den = compound.getDensity().doubleValue(); //will throw NullPointerException when the density does not exist.
			if (den == 0.0) {
				System.out.println("Encountered a compound with density 0: "+compound.getName()+". Question generation aborted.");
				return new Question("Error: compound without density", "Error: compound without density");
			};
		} catch (NullPointerException e){
			System.out.println("Encountered a compound without density. Question generation aborted.");
			return new Question("Error: compound without density", "Error: compound without density");
		}

		//density is saved in the compound with too many digits. Fewer digits are required for printing the question.
		BigDecimal density = new BigDecimal(compound.getDensity().toString(), threeDigit);

		BigDecimal volume = new BigDecimal(localRandomNums.nextDouble()*factorVolume, threeDigit);
		BigDecimal volumeBaseUnit = volume;
		String unitConversion = "";

		//Question unit selection. Make sure the last unit is ALWAYS the base unit!
		//This fact is used when adapting the answer to the unit.
		String[] units = {"mL", "L", "cm<sup>3</sup>", "m<sup>3</sup>"};
		int unitSelector = localRandomNums.nextInt(units.length);
		String unit = units[unitSelector];

		String text = String.format("Bereken de massa van %s %s %s bij 273 K",
				volume.toPlainString(),
				unit,
				compound.getHtmlFormula());

		//volume to mass: multiply by density
		//Attention! Density is always in kg/m3.
		if (unitSelector != units.length-1) { //aka: unit is not the base unit
			//Determine factor with which to scale the given unit to the base unit
			BigDecimal unitFactor;
			switch(unit) {
			case "mL":
			case "cm<sup>3</sup>":
				unitFactor = new BigDecimal("1E-6");
				break;
			case "L":
				unitFactor = new BigDecimal("1E-3");
				break;
			default:
				unitFactor = new BigDecimal("1");
				break;
			}

			volumeBaseUnit = volume.multiply(unitFactor, threeDigit);
			unitConversion = String.format("%s %s = %s m<sup>3</sup>",
					volume.toPlainString(),
					unit,
					volumeBaseUnit.toString());
			unitConversion = unitConversion + lineBreak; //this makes it fit with the next line of the answer

		}
		
		BigDecimal mass = volumeBaseUnit.multiply(compound.getDensity(), threeDigit);
		String baseUnitCalc = String.format("%s m<sup>3</sup> x %s kg/m<sup>3</sup> = %s kg",
				volumeBaseUnit.toString(),
				density.toString().replace("E+", "x10<sup>").concat("</sup>"),
				mass.toString().replace("E+", "x10<sup>").concat("</sup>"));

		String answer = unitConversion + baseUnitCalc;

		return new Question(text, answer);
	}
}
