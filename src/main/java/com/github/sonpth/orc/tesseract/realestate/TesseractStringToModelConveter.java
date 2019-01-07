package com.github.sonpth.orc.tesseract.realestate;

import java.util.Arrays;
import java.util.List;

import com.github.sonpth.orc.tesseract.realestate.model.RealEstate;

public class TesseractStringToModelConveter {

	public static RealEstate convert (String input) {
		List<String> data = Arrays.asList(input.split("\n"));
		RealEstate result = new RealEstate();
		final int noRows = data.size();
		result.setStreet(data.get(noRows - 1 - 2).replaceAll("(\\d+)(\\s*\\-\\s*)(\\d+)", "$1-$3"));
		result.setLocation(data.get(noRows - 1 - 1));
		return result;
	}
}
