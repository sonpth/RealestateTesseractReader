package com.github.sonpth.orc.tesseract.realestate;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.slf4j.LoggerFactory;

import com.github.sonpth.orc.tesseract.realestate.model.RealEstate;

import org.slf4j.Logger;

import static com.github.sonpth.orc.tesseract.realestate.TesseractReader.*;
import static com.github.sonpth.orc.tesseract.realestate.TesseractStringToModelConveter.*;

/**
 * This class is responsible to rename a auto-generated filename (e.g:
 * `SmartSelectImage_2018-10-25-08-16-02.png`) to something is more meaningful (e.g: ``).
 *
 * @author Phan Son <https://github.com/sonpth>
 */
public class FileOrganizer {
	private static final String inFolder = "/home/pson/tmp/data/Screenshots";
//	private static final String inFolder = "src/test/resources";
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
				final String inputFilename = inFolder + "/" + file.getName();
				Path sourcePath = Paths.get(inputFilename);

				RealEstate model = convert(extract(inputFilename));
				final String outputFilename = String.format("%s_%s%s",
						model.getLocation(),
						model.getStreet().replaceAll("/", " "),
						//file extension
						(idx = filename.lastIndexOf(".")) != -1 && idx < filename.length() - 1 ? "." + filename.substring(idx + 1) : "") ; 
				
				Path destinationPath = Paths.get(outFolder + "/" + outputFilename);

//				Files.move(sourcePath, destinationPath);
				Files.copy(sourcePath, destinationPath);
			} catch (Exception e) {
				LOGGER.warn(e.getMessage(), e);
			}
		}
		
		System.out.println(String.format("Total: %d files, elapsed: %d ms",
				files.length, System.currentTimeMillis() - start));
	}
}
