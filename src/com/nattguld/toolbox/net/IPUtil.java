package com.nattguld.toolbox.net;

import java.util.Objects;

import com.google.gson.JsonObject;
import com.nattguld.data.json.JsonReader;
import com.nattguld.http.HttpClient;
import com.nattguld.http.proxies.HttpProxy;
import com.nattguld.http.proxies.ProxyManager;
import com.nattguld.http.requests.impl.GetRequest;
import com.nattguld.http.response.RequestResponse;

/**
 * 
 * @author randqm
 *
 */

public class IPUtil {
	
	/**
	 * The proxy check API key.
	 */
	private static final String PROXY_CHECK_API_KEY = "53768b-267550-k35z14-433l9r";
	
	
	/**
	 * Retrieves whether a given IP is detected as anything.
	 * 
	 * @param ip The IP.
	 * 
	 * @return What the IP is detected as if anything.
	 */
	public static String checkIPDetection(String ip) {
		try (HttpClient c = new HttpClient()) {
			RequestResponse rr = c.dispatchRequest(new GetRequest("http://proxycheck.io/v2/" + ip 
					+ "?key=" + PROXY_CHECK_API_KEY + "&vpn=1&asn=0&node=0&time=0&inf=0&tag=default"));
    		
    		if (!rr.validate()) {
    			return "Failed to check IP detection for " + ip;
    		}
    		JsonReader jsonReader = rr.getJsonReader();
    		
    		String status = jsonReader.getAsString("status");
    		
    		if (!status.equalsIgnoreCase(status)) {
    			return "Unexpected status: " + status + " while checking detection for " + ip;
    		}
    		JsonObject detailsObj = jsonReader.getAsJsonObject(ip);
    		
    		String detected = detailsObj.get("proxy").getAsString();
    		
    		if (detected.equalsIgnoreCase("yes")) {
    			String type = detailsObj.get("type").getAsString();
        		@SuppressWarnings("unused")
    			String provider = detailsObj.get("provider").getAsString();
        		
    			return "Detected as " + type;
    		}
    		return "";
		}
	}
	
	/**
	 * Fetches the localhost IP.
	 * 
	 * @return The IP.
	 */
	public static String fetchIP() {
		return fetchIP(ProxyManager.INVALID_PROXY);
	}
	
	/**
	 * Fetches the IP for a given http client session.
	 * 
	 * @param c The http client session.
	 * 
	 * @return The IP.
	 */
	public static String fetchIP(HttpClient c) {
		return fetchIP(c.getProxy());
	}
	
	/**
	 * Fetches the IP for a given proxy.
	 * 
	 * @param proxy The proxy.
	 * 
	 * @return The IP.
	 */
	public static String fetchIP(HttpProxy proxy) {
		proxy = (Objects.isNull(proxy) || proxy == ProxyManager.INVALID_PROXY) ? null : proxy;
		
		try (HttpClient c = new HttpClient(proxy)) {
			RequestResponse rr = c.dispatchRequest(new GetRequest("https://api.ipify.org/"));
    		
    		if (!rr.validate()) {
    			return "Failed to fetch IP";
    		}
    		return rr.getResponseContent();
		}
	}

}
