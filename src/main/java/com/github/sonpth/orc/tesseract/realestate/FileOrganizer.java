package com.github.sonpth.orc.tesseract.realestate;

import static com.github.sonpth.orc.tesseract.realestate.TesseractReader.extract;
import static com.github.sonpth.orc.tesseract.realestate.TesseractStringToModelConveter.convert;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.sonpth.orc.tesseract.realestate.model.RealEstate;

/**
 * This class is responsible to rename a auto-generated filename (e.g:
 * `SmartSelectImage_2018-10-25-08-16-02.png`) to something is more meaningful (e.g: ``).
 *
 * @author Phan Son <https://github.com/sonpth>
 */
public class FileOrganizer {
	private static final String inFolder = "/home/pson/tmp/data/Screenshots";
//	private static final String inFolder = "/home/pson/tmp/data/input";
	private static final String outFolder = "/home/pson/tmp/data/output";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FileOrganizer.class);
	
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		final File folder = new File(inFolder);
		
		if (!folder.exists() || !folder.isDirectory()) {
			throw new IllegalArgumentException("The input folder must be exist and accessible");
		}
		
		final File[] files = folder.listFiles(); 
		for (File file: files) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Processing file {} ...", file.getName());
			}
			
			int idx;
			try {
				final String filename = file.getName();
				final String inputFilePath = inFolder + "/" + file.getName();
				Path sourcePath = Paths.get(inputFilePath);

				RealEstate model = convert(extract(inputFilePath));
				if (model.getLocation() == null 
						|| model.getStreet() == null) {
					LOGGER.warn("Could not extract information from [{}], manual intervention is required!", filename);
					continue;
				}
				
				//E.g: ".png"
				final String fileExt = (idx = filename.lastIndexOf(".")) != -1 && idx < filename.length() - 1 ? "." + filename.substring(idx + 1) : ""; 
				final String outputFilename = String.format("%s_%s%s",
						model.getLocation(),
						model.getStreet().replaceAll("/", " "),
						fileExt); 
				
				String outputFilePath = outFolder + "/" + outputFilename;
				//If the file is already exists (e.g: ad & sold) 
				if (new File(outputFilePath).exists()) {
					//"abc.png" > "abc_999.png"
					outputFilePath = outFolder + "/" 
							+ outputFilename.substring(0, outputFilename.length() - fileExt.length())
							+ "_" +  System.currentTimeMillis() 
							+ fileExt;
				}
				Path destinationPath = Paths.get(outputFilePath);

				Files.move(sourcePath, destinationPath);
//				Files.copy(sourcePath, destinationPath);
			} catch (Exception e) {
				LOGGER.warn(e.getMessage(), e);
			}
		}
		
		System.out.println(String.format("Total: %d files, elapsed: %d ms",
				files.length, System.currentTimeMillis() - start));
	}
}
