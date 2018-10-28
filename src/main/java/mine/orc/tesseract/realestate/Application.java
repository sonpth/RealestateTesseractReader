package mine.orc.tesseract.realestate;

import static org.bytedeco.javacpp.lept.pixDestroy;
import static org.bytedeco.javacpp.lept.pixRead;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.lept.PIX;
import org.bytedeco.javacpp.tesseract.TessBaseAPI;

import mine.orc.tesseract.realestate.model.RealEstate;

public class Application {
	public static void main(String[] args) {
		String result = extract("src/test/resources/SmartSelectImage_2018-10-25-08-16-02.png");
		String[] buf = result.split("\n");
		for (String str: buf) {
			System.out.println(str);
		}
	}
	
	//TODO what can be reused ?
	public static String extract(String filename) {
        BytePointer outText;

        TessBaseAPI api = new TessBaseAPI();
        // Initialize tesseract-ocr with English, without specifying tessdata path
        if (api.Init(".", "ENG") != 0) {
            System.err.println("Could not initialize tesseract.");
            System.exit(1);
        }

        // Open input image with leptonica library
        PIX image = pixRead(filename);
        api.SetImage(image);
        // Get OCR result
        outText = api.GetUTF8Text();
        String string = outText.getString();
        assertTrue(!string.isEmpty());
        System.out.println("OCR output:\n" + string);

        // Destroy used object and release memory
        api.End();
        outText.deallocate();
        pixDestroy(image);
        
        return string;
	}
	
	public static RealEstate extract2 (String input) {
		List<String> data = Arrays.asList(input.split("\n"));
		RealEstate result = new RealEstate();
		final int noRows = data.size();
		result.setStreet(data.get(noRows - 1 - 2));
		result.setLocation(data.get(noRows - 1 - 1));
		return result;
	}
}
