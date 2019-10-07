package com.nattguld.toolbox.text;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.nattguld.util.files.FileOperations;

/**
 * 
 * @author randqm
 *
 */

public class BulkFileUtil {
	
	
	/**
	 * Adds random bytes to files.
	 * 
	 * @param dir The start directory.
	 * 
	 * @param outputDir The output directory.
	 * 
	 * @param recursive Whether to search files recursively or not.
	 */
	public static void addRandomBytes(File dir, File outputDir, boolean recursive) {
		List<File> files = new ArrayList<>();
		
		if (!recursive) {
			for (File f : dir.listFiles()) {
				files.add(f);
			}
		} else {
			files = FileOperations.fetchFileTree(dir, true);
		}
		for (File f : files) {
			File out = FileOperations.addRandomBytes(f, new File(outputDir.getAbsolutePath() + File.separator + f.getName()));
			
			if (!out.exists()) {
				System.err.println("Failed to add random bytes to " + out.getAbsolutePath());
			}
		}
		System.out.println("Finished adding random bytes to " + files.size() + " files.");
	}
	
	/**
	 * Strips the EXIF of the files in a given directory.
	 * 
	 * @param dirPath The directory path.
	 */
	public static void stripEXIF(String dirPath) {
		File dir = new File(dirPath);
		File[] files = new File[1];
		
		if (dir.isDirectory()) {
			files = dir.listFiles();
		} else {
			files[0] = dir;
		}
		for (File f : files) {
			String extension = FileOperations.getExtension(f);
			
			try {
				BufferedImage image = ImageIO.read(f);
				ImageIO.write(image, extension, f);
				
			} catch (Exception ex) {
				ex.printStackTrace();
				System.err.println("Failed to strip EXIF for " + f.getName());
			}
		}
		System.out.println("Finished stripping EXIF in " + dirPath);
	}

}
