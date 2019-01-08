package com.github.sonpth.orc.tesseract.realestate;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.sonpth.orc.tesseract.realestate.model.RealEstate;

public class TesseractStringToModelConveter {
	private static Pattern LOC_PATTERN = Pattern.compile("(?<locality>([a-zA-Z]|\\s)+)(?<postcode>\\d{4}$)?");
	private static Pattern DATA_PATTERN = Pattern.compile("^[a-zA-Z$1-9].*");
	private static Logger LOGGER = LoggerFactory.getLogger(TesseractStringToModelConveter.class);


	public static RealEstate convert (String input) {
		List<String> data = removeNoise(input);
		RealEstate result = new RealEstate();
		if (data.isEmpty()) {
			return result;
		}
		int idx = 0;
		//extra info, skip by now
		if (data.get(idx).startsWith("Land")) {
			idx++;
		}
		if (data.get(idx).startsWith("Q")) {
			idx++;
		}
		
		//normalize locality
		final String proposedLocation = data.get(idx).replace("ﬁ", "fi");
		Matcher matcher = LOC_PATTERN.matcher(proposedLocation);
		if (matcher.matches()) {
			result.setLocation(matcher.group("locality").trim());
			if (matcher.group("postcode") != null) {
				result.setPostcode(matcher.group("postcode"));
			}
			idx++;
		} else {
			LOGGER.warn("Could not found location infor for [{}]", proposedLocation);
		}
		
		//normalize street-number-range
		final String proposedStreet = data.get(idx); 
		result.setStreet(proposedStreet.replaceAll("(\\d+)(\\s*\\-\\s*)(\\d+)", "$1-$3").trim());
		
		return result;
	}
	
	/**
	 * Since there is quite subtle of noise and optional extra information (e.g: agent-name),
	 * we revert to lines order. So we put data in this order: facility > locality > street > etc.
	 * @param input
	 * @return
	 */
	private static List<String> removeNoise(String input){
		String[] buf = input.split("\n");
		List<String> result = new ArrayList<>();
		
		for(int i = buf.length - 1; i >= 0; i--) {
			final String data = buf[i].trim();
			if (!data.isEmpty() && DATA_PATTERN.matcher(data).matches()) {
				result.add(data);
			}
		}
		if (LOGGER.isDebugEnabled()) {
			StringBuilder sb = new StringBuilder();
			for(String elm: result) {
				sb.append("\n\t").append(elm);
			}
			LOGGER.debug(sb.toString());
		}
		
		return result;
	}
	
	public static void main (String[] args) {
//		final String data = "Ashﬁeld 2131";
		final String data = "Ashﬁeld2131";
		System.out.println(LOC_PATTERN.matcher(data).matches());
	}
}
