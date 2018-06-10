package net.aionstudios.aosarachne;

import net.aionstudios.aosarachne.content.Page;
import net.aionstudios.aosarachne.crawling.Crawler;
import net.aionstudios.aosarachne.crawling.CrawlerQueue;
import net.aionstudios.aosarachne.domain.DomainManager;
import net.aionstudios.aosarachne.service.PageStorageService;
import net.aionstudios.aosarachne.util.URLUtils;
import net.aionstudios.api.API;

public class Arachne {
	
	public static void main(String[] args) {
		API.initAPI("AOS Arachne", 26727, true, "Arachne");
		PageStorageService.createTables();
		Page p = new Page("http://hornsofhavoc3393.com/", true);
		Page p2 = new Page("http://microsoft.com/", true);
		Page p3 = new Page("http://google.com/", true);
		Page p4 = new Page("http://bamfordfoundation.org/", true);
		Page p5 = new Page("http://stackoverflow.com/", true);
		Page p6 = new Page("http://youtube.com/", true);
		Page p7 = new Page("http://github.com/", true);
		Page p8 = new Page("http://reddit.com/", true);
		Page p1 = new Page("http://oracle.com/", true);
		Page p12 = new Page("http://twitter.com/", true);
		Page p13 = new Page("http://craigslist.org/", true);
		Page p14 = new Page("http://yahoo.com/", true);
		Page p15 = new Page("http://battle.net/", true);
		Page p16 = new Page("http://steampowered.com/", true);
		Page p17 = new Page("http://w3schools.com/", true);
		Page p18 = new Page("http://moz.com/", true);
		Crawler.initialize();
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				while(ArachneGlobals.running) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("Report: D"+DomainManager.getDomainCount()+" C"+CrawlerQueue.getCrawledSize()+" P"+CrawlerQueue.getQueueSize());
				}
			}
			
		});
		t.start();
	}

}
