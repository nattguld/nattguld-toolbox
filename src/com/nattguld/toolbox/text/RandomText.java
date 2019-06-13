package com.nattguld.toolbox.text;

import com.nattguld.http.HttpClient;
import com.nattguld.http.content.bodies.FormBody;
import com.nattguld.http.requests.impl.GetRequest;
import com.nattguld.http.requests.impl.PostRequest;
import com.nattguld.http.response.RequestResponse;
import com.nattguld.util.text.TextUtil;

/**
 * 
 * @author randqm
 *
 */

public class RandomText {

	
	/**
	 * Retrieves a random paragraph.
	 * 
	 * @return The paragraph.
	 */
	public static String getRandomParagraph() {
		return getRandomText(true);
	}
	
	/**
	 * Retrieves a random word.
	 * 
	 * @return The word.
	 */
	public static String getRandomWord() {
		return getRandomText(false);
	}
	
	/**
	 * Retrieves random text.
	 * 
	 * @param paragraph Whether it should be a paragraph or not.
	 * 
	 * @return The random text.
	 */
	private static String getRandomText(boolean paragraph) {
		try (HttpClient c = new HttpClient()) {
			RequestResponse rr = c.dispatchRequest(new GetRequest("http://watchout4snakes.com/wo4snakes/Random/RandomWord"));
			
			if (!rr.validate()) {
				System.err.println("Failed to load random word page (" + rr.getCode() + ")");
				return TextUtil.generatePassword();
			}
			if (paragraph) {
				FormBody fb = new FormBody();
				fb.add("Subject1", "");
				fb.add("Subject2", "");
				
				rr = c.dispatchRequest(new PostRequest("http://watchout4snakes.com/wo4snakes/Random/RandomParagraph", 200, fb).setXMLHttpRequest(true));
				
				if (!rr.validate()) {
					System.err.println("Failed to request random paragraph (" + rr.getCode() + ")");
					return TextUtil.generatePassword();
				}
				return rr.getResponseContent();
			}
			FormBody fb = new FormBody();
			fb.add("LastWord", "");
			
			rr = c.dispatchRequest(new PostRequest("http://watchout4snakes.com/wo4snakes/Random/RandomWord", 200, fb).setXMLHttpRequest(true));
			
			if (!rr.validate()) {
				System.err.println("Failed to request random word (" + rr.getCode() + ")");
				return TextUtil.generatePassword();
			}
			return rr.getResponseContent();
		}
	}
	
}
