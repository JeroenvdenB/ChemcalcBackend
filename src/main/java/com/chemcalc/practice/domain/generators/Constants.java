package com.chemcalc.practice.domain.generators;

import java.math.BigDecimal;
import java.math.MathContext;

public interface Constants {
	//Constants used in question generators
	public final MathContext fiveDigit = new MathContext(5);		
	public final MathContext fourDigit = new MathContext(4);		
	public final MathContext threeDigit = new MathContext(3);
	public final MathContext twoDigit = new MathContext(2); 		
	
	public final BigDecimal avogadro = new BigDecimal("6.02214076E23");
	public final BigDecimal molarVolume = new BigDecimal("0.0245", threeDigit);
	public final String lineBreak = "</br>&nbsp;&nbsp;";
}
