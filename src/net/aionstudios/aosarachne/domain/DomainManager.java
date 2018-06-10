package net.aionstudios.aosarachne.domain;

import java.util.LinkedList;
import java.util.List;

import net.aionstudios.aosarachne.util.URLUtils;

public class DomainManager {
	
	private static List<Domain> domains = new LinkedList<Domain>();
	
	public static Domain getDomain(String url, boolean createIfNot) {
		for(Domain d : domains) {
			if(d.getDomain().equals(URLUtils.getDomainName(url))) {
				return d;
			}
		}
		if(createIfNot) {
			Domain d = new Domain(url);
			domains.add(0, d);
			return d;
		}
		return null;
	}
	
	public static long getCrawlDelayCalculated() {
		return 20+domains.size()*10;//10:34pm 6/4/2018
	}
	
	public static boolean removeDomain(String url) {
		for(Domain d : domains) {
			if(d.getDomain().equals(URLUtils.getDomainName(url))) {
				domains.remove(d);
				return true;
			}
		}
		return false;
	}
	
	public static Domain getNextDomain() {
		if(domains.size()>0) {
			return domains.get(0);
		} else {
			return null;
		}
	}
	
	public static void putDomainAtEnd(String url) {
		int location = -1;
		for(Domain d : domains) {
			location++;
			if(d.getDomain().endsWith(URLUtils.getDomainName(url))) {
				domains.add(domains.size(), d);
				domains.remove(location);
			}
		}
	}
	
	public static int getDomainCount() {
		return domains.size();
	}

}
