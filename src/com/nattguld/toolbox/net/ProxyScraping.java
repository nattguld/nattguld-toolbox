package com.nattguld.toolbox.net;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jsoup.nodes.Element;

import com.nattguld.http.HttpClient;
import com.nattguld.http.proxies.HttpProxy;
import com.nattguld.http.proxies.standard.StandardProxyManager;
import com.nattguld.http.requests.impl.GetRequest;
import com.nattguld.http.response.RequestResponse;

/**
 * 
 * @author randqm
 *
 */

public class ProxyScraping {
	
	
	/**
	 * Scrapes proxies and retrieved the valid ones.
	 * 
	 * @param amount The amount to scrape.
	 * 
	 * @return The valid scraped proxies.
	 */
	public static List<HttpProxy> scrapeAndValidateProxies(int amount) {
		return ProxyValidation.retrieveValidProxies(scrapeProxies(amount));
	}
	
	/**
	 * Scrapes & retrieves the scraped proxies.
	 * 
	 * @param amount The amount of proxies to scrape.
	 * 
	 * @return The scraped proxies.
	 */
	public static List<HttpProxy> scrapeProxies(int amount) {
		List<HttpProxy> proxies = new ArrayList<>();
		
		try (HttpClient c = new HttpClient()) {
			RequestResponse rr = c.dispatchRequest(new GetRequest("https://proxy.rudnkh.me/txt"));
			
			if (rr.validate()) {
				for (String line : rr.getResponseContent().split("\\n")) {
					HttpProxy proxy = StandardProxyManager.getSingleton().parse(line);
					
					if (Objects.isNull(proxy)) {
						continue;
					}
					proxies.add(proxy);
				}
			} else {
				System.err.println("Failed to scrape proxies from proxy.rudnkh.me");
			}
		}
		if (proxies.size() >= amount) {
			System.out.println("Scraped " + proxies.size() + " proxies");
			return proxies;
		}
		try (HttpClient c = new HttpClient()) {
			RequestResponse rr = c.dispatchRequest(new GetRequest("http://multiproxy.org/txt_all/proxy.txt"));
			
			if (rr.validate()) {
				for (String line : rr.getResponseContent().split("\\n")) {
					HttpProxy proxy = StandardProxyManager.getSingleton().parse(line);
				
					if (Objects.isNull(proxy)) {
						continue;
					}
					proxies.add(proxy);
				}
			} else {
				System.err.println("Failed to scrape proxies from multiproxy.org");
			}
		}
		if (proxies.size() >= amount) {
			System.out.println("Scraped " + proxies.size() + " proxies");
			return proxies;
		}
		try (HttpClient c = new HttpClient()) {
			RequestResponse rr = c.dispatchRequest(new GetRequest("https://raw.githubusercontent.com/clarketm/proxy-list/master/proxy-list.txt"));
			
			if (rr.validate()) {
				for (String line : rr.getResponseContent().split("\\n")) {
					if (line.startsWith("Proxy") || line.startsWith("Mirrors") || line.startsWith("IP address") || line.isEmpty()) {
						continue;
					}
					String format = line.split(" ")[0];
					HttpProxy proxy = StandardProxyManager.getSingleton().parse(format);
				
					if (Objects.isNull(proxy)) {
						continue;
					}
					proxies.add(proxy);
				}
			} else {
				System.err.println("Failed to scrape proxies from raw.githubusercontent.com");
			}
		}
		if (proxies.size() >= amount) {
			System.out.println("Scraped " + proxies.size() + " proxies");
			return proxies;
		}
		try (HttpClient c = new HttpClient()) {
			RequestResponse rr = c.dispatchRequest(new GetRequest("http://proxy-daily.com/"));
			
			if (rr.validate()) {
				Element id = rr.getAsDoc().getElementById("free-proxy-list");
				Element div = id.getElementsByTag("div").first();
			
				for (String line : div.text().split("\\n")) {
					HttpProxy proxy = StandardProxyManager.getSingleton().parse(line);
				
					if (Objects.isNull(proxy)) {
						continue;
					}
					proxies.add(proxy);
				}
			} else {
				System.err.println("Failed to scrape proxies from proxy-daily.com");
			}
		}
		if (proxies.size() >= amount) {
			System.out.println("Scraped " + proxies.size() + " proxies");
			return proxies;
		}
		if (proxies.isEmpty()) {
			System.err.println("Failed to scrape any proxies");
			return proxies;
		}
		System.out.println("Scraped " + proxies.size() + " proxies");
		return proxies;
	}

}
