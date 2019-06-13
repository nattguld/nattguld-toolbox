package com.nattguld.toolbox.text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.nattguld.util.files.FileOperations;

/**
 * 
 * @author randqm
 *
 */

public class TextHandler {
	
	
	/**
	 * Replaces the text in a file.
	 * 
	 * @param filePath The file path.
	 * 
	 * @param textToReplace The text to replace.
	 * 
	 * @param replaceText The replacement text.
	 * 
	 * @param caseSensitive Whether to search case sensitive or not.
	 */
	public static void replaceTextInFile(String filePath, String textToReplace, String replaceText, boolean caseSensitive) {
		String content = FileOperations.getContent(filePath);
		
		FileOperations.write(filePath, replaceText(content, textToReplace, replaceText, caseSensitive), false);
		
		System.out.println("Finished replacing text in " + filePath);
	}
	
	/**
	 * Replaces the text in a file.
	 * 
	 * @param text The original text.
	 * 
	 * @param textToReplace The text to replace.
	 * 
	 * @param replaceText The replacement text.
	 * 
	 * @param caseSensitive Whether to search case sensitive or not.
	 */
	public static String replaceText(String text, String textToReplace, String replaceText, boolean caseSensitive) {
		return text.replaceAll((caseSensitive ? "(?i)" : "") + Pattern.quote(textToReplace), replaceText);
	}
	
	/**
	 * Removes duplicate lines in a file.
	 * 
	 * @param filePath The file path.
	 * 
	 * @param caseSensitive Whether to search case sensitive or not.
	 */
	public static void removeDuplicateLines(String filePath, boolean caseSensitive) {
		List<String> lines = removeDuplicateLines(FileOperations.read(filePath), caseSensitive);
		
		FileOperations.write(filePath, lines, false);
		
		System.out.println("Removed duplicate lines from " + filePath);
	}
	
	/**
	 * Removes duplicates from a list of lines.
	 * 
	 * @param lines The lines.
	 * 
	 * @param caseSensitive Whether to search case sensitive or not.
	 * 
	 * @return A list of lines with the duplicates removed.
	 */
	public static List<String> removeDuplicateLines(List<String> lines, boolean caseSensitive) {
		List<String> filter = new ArrayList<>();
		
		for (String line : lines) {
			if (filter.contains(line)) {
				continue;
			}
			filter.add(line);
		}
		return filter;
	}
	
	/**
	 * Renames the files in a given directory.
	 * 
	 * @param dirPath The directory path.
	 * 
	 * @param baseName The base name.
	 * 
	 * @param delimiter The delimiter.
	 */
	public static void renameFiles(File dirPath, String baseName, String delimiter) {
		int counter = 1;
		
		for (File f : dirPath.listFiles()) {
			if (f.isDirectory()) {
				continue;
			}
			String fileName = baseName + (counter == 0 ? "" : (delimiter + counter)) + "." + FileOperations.getExtension(f);
			File out = FileOperations.rename(f, new File(f.getAbsolutePath().replace(f.getName(), fileName)));
			
			if (f.getName().equals(out.getName())) {
				System.err.println("Failed to rename " + f.getName());
			}
		}
	}

}
