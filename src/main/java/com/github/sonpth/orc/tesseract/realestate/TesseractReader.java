package com.github.sonpth.orc.tesseract.realestate;

import static org.bytedeco.javacpp.lept.pixDestroy;
import static org.bytedeco.javacpp.lept.pixRead;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.lept.PIX;
import org.bytedeco.javacpp.tesseract.TessBaseAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TesseractReader {
	private static Logger LOGGER = LoggerFactory.getLogger(TesseractReader.class);

	/**
	 * @param filename - path to the image which we want to exact data from
	 * @return a {@code String} is a result of Tesseract API.
	 * 
	 * TODO what can be reused
	 */
	public static String extract(String filename) {
		BytePointer outText;

		TessBaseAPI api = new TessBaseAPI();
		// Initialize tesseract-ocr with English, without specifying tessdata path
		if (api.Init(".", "ENG") != 0) {
			throw new RuntimeException("Could not initialize tesseract.");
		}

		// Open input image with leptonica library
		PIX image = pixRead(filename);
		api.SetImage(image);
		int height = image.h() / 2;
		api.SetRectangle(0, height, image.w(), height);
		
		// Get OCR result
		outText = api.GetUTF8Text();
		String string = outText.getString();
		LOGGER.debug("OCR output:\n {}", string);

		// Destroy used object and release memory
		api.End();
		outText.deallocate();
		pixDestroy(image);

		return string;
	}

}
