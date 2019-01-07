package com.github.sonpth.orc.tesseract.realestate;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.github.sonpth.orc.tesseract.realestate.model.RealEstate;

public class ApplicationTest {

	@Test
	public void testSold1() {
		RealEstate harness = TesseractStringToModelConveter
				.convert(TesseractReader.extract("src/test/resources/SmartSelectImage_2018-10-25-08-16-02.png"));
		assertEquals("Artarmon", harness.getLocation());
		assertEquals("143/421-473 Paciﬁc Highway", harness.getStreet());
	}

	@Test
	public void testSold2() {
		RealEstate harness = TesseractStringToModelConveter
				.convert(TesseractReader.extract("src/test/resources/SmartSelectImage_2018-10-25-08-15-50.png"));
		assertEquals("Lane Cove", harness.getLocation());
		assertEquals("39/316 Paciﬁc Highway", harness.getStreet());
	}
}
