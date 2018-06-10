package net.aionstudios.aosarachne.networking;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

import net.aionstudios.aosarachne.ArachneGlobals;

public class PageRequester {
	
	public static String fetchPage(String pageUrl) {
		try {
			URL url = new URL(pageUrl);
	        Map<String,Object> params = new LinkedHashMap<>();
	        HttpURLConnection.setFollowRedirects(false);
	        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
	        conn.setRequestMethod("POST");
	        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	        conn.setRequestProperty("User-Agent", ArachneGlobals.USER_AGENT+"/"+ArachneGlobals.ARACHNE_VER);
	        conn.setConnectTimeout(5000);
	        conn.setDoOutput(true);

	        Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

	        StringBuilder sb = new StringBuilder();
	        for (int c; (c = in.read()) >= 0;)
	           sb.append((char)c);
	        if(conn.getResponseCode()==HttpURLConnection.HTTP_OK)
	        	return sb.toString();
	        return null;
		} catch(Exception e) {
			return null;
		}
	}

}
