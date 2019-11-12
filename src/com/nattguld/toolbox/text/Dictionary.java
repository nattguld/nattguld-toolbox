package com.nattguld.toolbox.text;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.nattguld.data.json.JsonReader;
import com.nattguld.http.HttpClient;
import com.nattguld.http.requests.impl.GetRequest;
import com.nattguld.http.response.RequestResponse;
import com.nattguld.util.maths.Maths;

/**
 * 
 * @author randqm
 *
 */

public class Dictionary {
	
	
	/**
	 * Refactors a given input.
	 * 
	 * @param input The input.
	 * 
	 * @return The refactored input.
	 */
	public static String refactor(String input) {
		String[] words = input.split(" ");
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < words.length; i++) {
			String word = words[i];
			List<String> options = new LinkedList<>();
			
			if (words.length > 1) {
				if (i == 0) {
					options = fetchReplacement(word, "", words[i + 1]);
					
				} else if (i == (words.length - 1)) {
					options = fetchReplacement(word, words[words.length - 2], "");
					
				} else {
					options = fetchReplacement(word, words[i - 1], words[i + 1]);
				}
			} else {
				options = fetchReplacement(word, "", "");
			}
			String replacementWord = options.isEmpty() ? word : getReplacementWord(options, 0);
			sb.append(replacementWord);
			
			if (i < (words.length - 1) ) {
				sb.append(" ");
			}
		}
		return sb.toString().trim();
	}
	
	/**
	 * Retrieves a replacement word from a given list of words.
	 * 
	 * @param words The words.
	 * 
	 * @param index The current generation index.
	 * 
	 * @return The replacement word.
	 */
	private static String getReplacementWord(List<String> words, int index) {
		if (words.size() == (index + 1)) {
			return words.get(index);
		}
		if (Maths.random(100) < 67) {
			return words.get(index);
		}
		return getReplacementWord(words, ++index);
	}
	
	/**
	 * Fetches suggested words the input might form.
	 * 
	 * @param input The input.
	 * 
	 * @param before The word before the input.
	 * 
	 * @param after The word after the input.
	 * 
	 * @return The fetched words.
	 */
	public static List<String> fetchSuggestions(String input, String before, String after) {
		return fetch("sug?s", input, before, after);
	}
	
	/**
	 * Fetches words often used to describe a given input.
	 * 
	 * @param input The input.
	 * 
	 * @param before The word before the input.
	 * 
	 * @param after The word after the input.
	 * 
	 * @return The fetched words.
	 */
	public static List<String> fetchOftenUsedAdjective(String input, String before, String after) {
		return fetch("words?rel_jjb", input, before, after);
	}
	
	/**
	 * Fetches words with which rhyme to a given input.
	 * 
	 * @param input The input.
	 * 
	 * @param before The word before the input.
	 * 
	 * @param after The word after the input.
	 * 
	 * @return The fetched words.
	 */
	public static List<String> fetchRhyming(String input, String before, String after) {
		return fetch("words?rel_rhy", input, before, after);
	}
	
	/**
	 * Fetches words with a similar sound.
	 * 
	 * @param input The input.
	 * 
	 * @param before The word before the input.
	 * 
	 * @param after The word after the input.
	 * 
	 * @return The fetched words.
	 */
	public static List<String> fetchSimilarSound(String input, String before, String after) {
		return fetch("words?sl", input, before, after);
	}
	
	/**
	 * Fetches related words for a given input.
	 * 
	 * @param input The input.
	 * 
	 * @param before The word before the input.
	 * 
	 * @param after The word after the input.
	 * 
	 * @return The fetched words.
	 */
	public static List<String> fetchRelated(String input, String before, String after) {
		return fetch("words?ml", input, before, after);
	}
	
	/**
	 * Fetches a synonym for a given input.
	 * 
	 * @param input The input.
	 * 
	 * @param before The word before the input.
	 * 
	 * @param after The word after the input.
	 * 
	 * @return The fetched words.
	 */
	public static List<String> fetchSynonym(String input, String before, String after) {
		return fetch("words?rel_syn", input, before, after);
	}
	
	/**
	 * Fetches a synonym or related word for a given input.
	 * 
	 * @param input The input.
	 * 
	 * @param before The word before the input.
	 * 
	 * @param after The word after the input.
	 * 
	 * @return The fetched words.
	 */
	public static List<String> fetchReplacement(String input, String before, String after) {
		List<String> options = fetchSynonym(input, before, after);
		
		if (options.isEmpty()) {
			options = fetchRelated(input, before, after);
		}
		return options;
	}
	
	/**
	 * Fetches the results for a given input and endpoint.
	 * 
	 * @param endpoint The endpoint.
	 * 
	 * @param input The input.
	 * 
	 * @param before The word before the input.
	 * 
	 * @param after The word after the input.
	 * 
	 * @return The result.
	 */
	private static List<String> fetch(String endpoint, String input, String before, String after) {
		String baseUrl = "https://api.datamuse.com/" + endpoint + "=" + input.replace(" ", "+");
		
		if (Objects.nonNull(before) || Objects.nonNull(after)) {
			if (Objects.nonNull(before) && !before.isEmpty()) {
				baseUrl += "&lc=" + before;
			}
			if (Objects.nonNull(after) && !after.isEmpty()) {
				baseUrl += "&rc=" + after;
			}
		}
		List<String> results = new LinkedList<>();
		
		try (HttpClient c = new HttpClient()) {
			RequestResponse rr = c.dispatchRequest(new GetRequest(baseUrl));
			
			if (!rr.validate()) {
				return results;
			}
			JsonReader jsonReader = rr.getJsonReader();
			
			for (JsonElement el : jsonReader.getArray()) {
				JsonObject result = el.getAsJsonObject();
				
				results.add(result.get("word").getAsString());
			}
		}
		return results;
	}

}
