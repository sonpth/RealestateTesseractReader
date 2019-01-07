package com.github.sonpth.orc.tesseract;

import org.bytedeco.javacpp.*;
import org.junit.jupiter.api.Test;

import static org.bytedeco.javacpp.lept.*;
import static org.bytedeco.javacpp.tesseract.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 *	Original [https://github.com/piersy/BasicTesseractExample]
 *
 *	A simple idea case to show how the library and the wrapper works.  
 */
public class BasicTesseractExampleTest {
    
    @Test
    public void givenTessBaseApi_whenImageOcrd_thenTextDisplayed() throws Exception {
        BytePointer outText;

        TessBaseAPI api = new TessBaseAPI();
        // Initialize tesseract-ocr with English, without specifying tessdata path
        if (api.Init(".", "ENG") != 0) {
            System.err.println("Could not initialize tesseract.");
            System.exit(1);
        }

        // Open input image with leptonica library
        PIX image = pixRead("src/test/resources/test.png");
        api.SetImage(image);
        // Get OCR result
        outText = api.GetUTF8Text();
        String result = outText.getString();
        assertTrue(!result.isEmpty());
        System.out.println("OCR output:\n" + result);
        
        //Assertions
        String[] buf = result.split("\n");
        //It breaks the lines as in the image
        assertEquals("This is a lot of 12 point text to test the", buf[0]);
        //It adds an empty line between two paragraphs in this case.
        assertEquals(9, buf.length);
        assertEquals("", buf[3]);
        //The next paragraph
        assertEquals("The quick brown dog jumped over the", buf[4]);

        // Destroy used object and release memory
        api.End();
        outText.deallocate();
        pixDestroy(image);
    }
}