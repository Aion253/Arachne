package net.aionstudios.aosarachne.domain;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import com.panforge.robotstxt.MatchingStrategy;
import com.panforge.robotstxt.RobotsTxt;
import com.panforge.robotstxt.RobotsTxtReader;
import com.panforge.robotstxt.WinningStrategy;

import net.aionstudios.aosarachne.ArachneGlobals;
import net.aionstudios.aosarachne.content.Page;
import net.aionstudios.aosarachne.crawling.Crawler;
import net.aionstudios.aosarachne.crawling.CrawlerQueue;
import net.aionstudios.aosarachne.networking.PageRequester;
import net.aionstudios.aosarachne.util.URLUtils;

public class Domain {
	
	private String domain;
	private RobotsTxt robots;
	private long crawlDelayMillis = 0;
	private long lastCrawl = 0;
	
	public Domain(String domainUrl) {
		domain = URLUtils.getDomainName(domainUrl);
		String content = PageRequester.fetchPage("https://"+domainUrl+"robots.txt");
		if(content==null) {
			content = PageRequester.fetchPage("http://"+domainUrl+"robots.txt");
		}
		if(content==null) {
			return;
		}
		InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
		RobotsTxtReader reader = new RobotsTxtReader(MatchingStrategy.DEFAULT, WinningStrategy.DEFAULT);
		try {
			robots = reader.readRobotsTxt(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			if(robots.getCrawlDelay()!=0&&robots.getCrawlDelay()>0.5) {
				crawlDelayMillis = (long) robots.getCrawlDelay()*1000;
			}
		} catch (NullPointerException e) {}
	}
	
	public void updateLastCrawl() {
		lastCrawl = System.currentTimeMillis();
	}
	
	public boolean availableForCrawl() {
		return lastCrawl+getCrawlDelayMillis()<System.currentTimeMillis();
	}
	
	public boolean allowedCrawl(String url) {
		if(robots!=null)
			return robots.query("Arachne", URLUtils.getPathAndQuery(url));
		return true;
	}

	public String getDomain() {
		return domain;
	}

	public RobotsTxt getRobots() {
		return robots;
	}

	public long getCrawlDelayMillis() {
		if(crawlDelayMillis==0||crawlDelayMillis<DomainManager.getCrawlDelayCalculated()) {
			return DomainManager.getCrawlDelayCalculated();
		}
		return crawlDelayMillis;
	}

	public long getLastCrawl() {
		return lastCrawl;
	}
	
}
