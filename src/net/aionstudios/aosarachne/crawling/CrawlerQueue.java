package net.aionstudios.aosarachne.crawling;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.aionstudios.aosarachne.content.Page;
import net.aionstudios.aosarachne.domain.Domain;
import net.aionstudios.aosarachne.domain.DomainManager;

public class CrawlerQueue {
	
	private static List<Page> crawled = new LinkedList<Page>();
	private static int pageCursor = 0;
	private static List<Page> crawlQueue = new LinkedList<Page>();
	
	public static void resetPageCursor() {
		pageCursor = 0;
	}
	
	public static Page getNextPage() {
		if(crawlQueue.size()>pageCursor) {
			int cursor = pageCursor;
			pageCursor++;
			return crawlQueue.get(cursor);
		} else {
			resetPageCursor();
		}
		return null;
	}
	
	public static void addQueuePage(Domain d, Page p, boolean forceUpdateForAge) {
		if(d.allowedCrawl(p.getUrl())) {
			for(Page a : crawlQueue) {
				if(a.getUrl().equalsIgnoreCase(p.getUrl())) {
					return;
				}
			}
			for(Page a : crawled) {
				if(a.getUrl().equalsIgnoreCase(p.getUrl())) {
					return;
				}
			}
			//System.out.println(crawlQueue.size()+" "+p.getUrl());
			crawlQueue.add(p);
		}
	}
	
	public static void removeQueuePage(Page p) {
		for(Page a : crawlQueue) {
			if(a.getUrl().equalsIgnoreCase(p.getUrl())) {
				crawlQueue.remove(a);
				return;
			}
		}
	}
	
	public static void addCrawledPage(Domain d, Page p) {
		for(Page a : crawled) {
			if(a.getUrl().equalsIgnoreCase(p.getUrl())) {
				return;
			}
		}
		crawled.add(p);
		removeQueuePage(p);
	}
	
	public static void removeCrawledPage(Page p) {
		for(Page a : crawled) {
			if(a.getUrl().equalsIgnoreCase(p.getUrl())) {
				crawled.remove(a);
				return;
			}
		}
	}
	
	public static int getQueueSize() {
		return crawlQueue.size();
	}
	
	public static int getCrawledSize() {
		return crawled.size();
	}

}
