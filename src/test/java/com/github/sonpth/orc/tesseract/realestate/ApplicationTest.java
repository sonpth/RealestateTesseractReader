package com.github.sonpth.orc.tesseract.realestate;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.github.sonpth.orc.tesseract.realestate.model.RealEstate;

public class ApplicationTest {

	@Test
	public void testSold1() {
		RealEstate harness = TesseractStringToModelConveter
				.convert(TesseractReader.extract("src/test/resources/Artarmon_143 421-473 Paciﬁc Highway.png"));
		assertEquals("Artarmon", harness.getLocation());
		assertEquals("143/421-473 Paciﬁc Highway", harness.getStreet());
	}

	@Test
	public void testSold2() {
		RealEstate harness = TesseractStringToModelConveter
				.convert(TesseractReader.extract("src/test/resources/Lane Cove_39 316 Paciﬁc Highway.png"));
		assertEquals("Lane Cove", harness.getLocation());
		assertEquals("39/316 Paciﬁc Highway", harness.getStreet());
	}
	
	@Test
	public void testAd_Postcode() {
		RealEstate harness = TesseractStringToModelConveter
				.convert(TesseractReader.extract("src/test/resources/Bankstown_6 36-38 Chertsey Avenue.png"));
		assertEquals("Bankstown", harness.getLocation());
		assertEquals("6/36-38 Chertsey Avenue", harness.getStreet());
		assertEquals("2200", harness.getPostcode());
	}
	
	@Test
	public void testAd_AgentName() {
		RealEstate harness = TesseractStringToModelConveter
				.convert(TesseractReader.extract("src/test/resources/Marrickville_10 133 Meeks Road.png"));
		assertEquals("Marrickville", harness.getLocation());
		assertEquals("10/133 Meeks Road", harness.getStreet());
		assertEquals("2204", harness.getPostcode());
	}
	
	@Test
	public void testFailedFacility() {
		RealEstate harness = TesseractStringToModelConveter
				.convert(TesseractReader.extract("src/test/resources/Padstow_1 36 Uranus Road.png"));
		assertEquals("Padstow", harness.getLocation());
		assertEquals("1/36 Uranus Road", harness.getStreet());
		assertEquals("2211", harness.getPostcode());
	}
	
	@Test
	public void testExtraInfoBottom() {
		RealEstate harness = TesseractStringToModelConveter
				.convert(TesseractReader.extract("src/test/resources/Revesby_1 2a Victoria Street.png"));
		//TODO we have a list of locality and its postcode.
		assertEquals("Reves by", harness.getLocation());
		assertEquals("1/2a Victoria Street", harness.getStreet());
		assertEquals("2212", harness.getPostcode());
	}
	
	@Disabled
	@Test
	public void test() {
		RealEstate harness = TesseractStringToModelConveter
				.convert(TesseractReader.extract("/home/pson/tmp/data/Screenshots/test.png"));
		//TODO we have a list of locality and its postcode.
		assertEquals("Reves by", harness.getLocation());
		assertEquals("1/2a Victoria Street", harness.getStreet());
		assertEquals("2212", harness.getPostcode());
	}


}
