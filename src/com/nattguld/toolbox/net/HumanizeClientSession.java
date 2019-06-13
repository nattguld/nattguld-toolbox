package com.nattguld.toolbox.net;

import java.util.Objects;

import com.nattguld.http.HttpClient;
import com.nattguld.http.requests.impl.GetRequest;
import com.nattguld.http.response.RequestResponse;
import com.nattguld.toolbox.text.RandomText;
import com.nattguld.util.maths.Maths;

/**
 * 
 * @author randqm
 *
 */

public class HumanizeClientSession {
	
	
	/**
	 * Humanizes a given http client session by browsing urls.
	 * 
	 * @param c The client session.
	 * 
	 * @param customUrls The custom urls.
	 */
	public static void humanize(HttpClient c, String... customUrls) {
		for (int i = 0; i < (1 + Maths.random(5)); i ++) {
			visitUrl(c, "http://www.google.com/search?q=" + RandomText.getRandomWord());
		}
		visitUrl(c, "https://www.youtube.com/");
		visitUrl(c, "https://twitter.com/search?q=%23" + RandomText.getRandomWord() + "&src=typd");
		
		if (Objects.nonNull(customUrls)) {
			for (String url : customUrls) {
				visitUrl(c, url);
			}
		}
		System.out.println("Finished humanizing client session");
	}
	
	/**
	 * Visits a url.
	 * 
	 * @param c The client session.
	 * 
	 * @param url The url to visit.
	 */
	private static void visitUrl(HttpClient c, String url) {
		try {
			RequestResponse rr = c.dispatchRequest(new GetRequest(url));
			
			if (!rr.validate()) {
				System.err.println("Failed to visit " + url);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
