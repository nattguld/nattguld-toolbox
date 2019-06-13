package com.nattguld.toolbox.net;

import java.util.ArrayList;
import java.util.List;

import com.nattguld.http.HttpClient;
import com.nattguld.http.proxies.HttpProxy;
import com.nattguld.http.requests.impl.GetRequest;
import com.nattguld.http.response.RequestResponse;

/**
 * 
 * @author randqm
 *
 */

public class ProxyValidation {
	
	
	/**
	 * Retrieves the valid proxies for a given list of proxies.
	 * 
	 * @param proxies The proxies.
	 * 
	 * @return The valid proxies.
	 */
	public static List<HttpProxy> retrieveValidProxies(List<HttpProxy> proxies) {
		return validateProxies(proxies, true);
	}
	
	/**
	 * Retrieves the broken proxies for a given list of proxies.
	 * 
	 * @param proxies The proxies.
	 * 
	 * @return The broken proxies.
	 */
	public static List<HttpProxy> retrieveBrokenProxies(List<HttpProxy> proxies) {
		return validateProxies(proxies, false);
	}
	
	/**
	 * Validates a given list of proxies and retrieves either the valid or broken ones.
	 * 
	 * @param proxies The proxies.
	 * 
	 * @param retrieveValid whether to retrieve the valid or broken proxies.
	 * 
	 * @return The proxies.
	 */
	private static List<HttpProxy> validateProxies(List<HttpProxy> proxies, boolean retrieveValid) {
		List<HttpProxy> filter = new ArrayList<>();
		
		for (HttpProxy proxy : proxies) {
			boolean valid = validateProxy(proxy);
			
			if ((!retrieveValid && !valid) || (retrieveValid && valid)) {
				filter.add(proxy);
			}
		}
		return filter;
	}
	
	/**
	 * Retrieves whether a given proxy is valid or not.
	 * 
	 * @param proxy The proxy.
	 * 
	 * @return The result.
	 */
	private static boolean validateProxy(HttpProxy proxy) {
		try (HttpClient c = new HttpClient(proxy)) {
			try {
				RequestResponse rr = c.dispatchRequest(new GetRequest("http://www.whatismyproxy.com/"));
				
				if (!rr.validate()) {
					return false;
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				return false;
			}
		}
		return true;
	}

}
