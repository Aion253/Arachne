package net.aionstudios.aosarachne.util;

import java.net.URI;
import java.net.URISyntaxException;

public class URLUtils {
	
	public static String getDomainName(String url) {
	    URI uri;
		try {
			if(!url.startsWith("http")) {
				url="http://"+url;
			}
			uri = new URI(url);
			String domain = uri.getHost();
			return domain.startsWith("www.") ? domain.substring(4) : domain;
		} catch (URISyntaxException e) {
			return null;
		}
	}
	
	public static String stripRequestMethod(String url) {
	    URI uri;
		try {
			if(!url.startsWith("http")) {
				url="http://"+url;
			}
			uri = new URI(url);
			String domain = uri.getHost()+uri.getPath();
			if(uri.getRawQuery()!=null) {
				domain = domain+"?"+uri.getRawQuery();
			}
			return domain.startsWith("www.") ? domain.substring(4) : domain;
		} catch (URISyntaxException e) {
			return null;
		}
	}
	
	public static String getPathAndQuery(String url) {
		URI uri;
		try {
			if(!url.startsWith("http")) {
				url="http://"+url;
			}
			uri = new URI(url);
			String domain = uri.getPath();
			if(uri.getRawQuery()!=null) {
				domain = domain+"?"+uri.getRawQuery();
			}
			return domain;
		} catch (URISyntaxException e) {
			return null;
		}
	}

}
