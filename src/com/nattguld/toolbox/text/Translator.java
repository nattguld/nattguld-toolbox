package com.nattguld.toolbox.text;

import java.io.IOException;
import java.net.URLEncoder;

import org.jsoup.nodes.Document;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.nattguld.http.HttpClient;
import com.nattguld.http.requests.impl.GetRequest;
import com.nattguld.http.response.RequestResponse;
import com.nattguld.util.locale.Language;

/**
 * 
 * @author randqm
 *
 */

public class Translator {
	
	
	/**
	 * Retrieves a translation in a given language.
	 * 
	 * @param text The text.
	 * 
	 * @param to The language.
	 * 
	 * @return The translated text.
	 */
	public static String translate(String text, Language to) {
		return translate(text, Language.ENGLISH, to);
	}
	
	/**
	 * Retrieves a translation from to another language.
	 * 
	 * @param text The text.
	 * 
	 * @param from The original language.
	 * 
	 * @param to The language.
	 * 
	 * @return The translated text.
	 */
	public static String translate(String text, Language from, Language to) {
		try (HttpClient c = new HttpClient()) {
			RequestResponse rr = c.dispatchRequest(new GetRequest("https://translate.googleapis.com/translate_a/single?client=gtx&sl=" 
					+ from.getCode() + "&tl=" + to.getCode() + "&dt=t&q=" + URLEncoder.encode(text, "UTF-8")));
			
			if (!rr.validate()) {
				System.err.println("Failed to translate " + text);
				return text;
			}
			Document doc = rr.getAsDoc();
			String json = doc.text();
			json = json.substring(2, json.indexOf("]") + 1);
			
			JsonArray jsonArr = new JsonParser().parse(json).getAsJsonArray();
			
			String translatedText = jsonArr.get(0).getAsString();
			
			System.out.println("Translated: [" + from.getName() + " -> " + to.getName() + "][" + text + " -> " + translatedText + "]");
			return translatedText;
			
		} catch (IOException ex) {
			ex.printStackTrace();
			return text;
		}
	}

}
