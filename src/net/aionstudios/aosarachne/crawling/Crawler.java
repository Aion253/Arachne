package net.aionstudios.aosarachne.crawling;

import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import net.aionstudios.aosarachne.ArachneGlobals;
import net.aionstudios.aosarachne.content.Page;
import net.aionstudios.aosarachne.domain.Domain;
import net.aionstudios.aosarachne.domain.DomainManager;
import net.aionstudios.aosarachne.util.URLUtils;

public class Crawler {
	
	private static ExecutorService es;
	private static ReentrantLock lock = new ReentrantLock();
	
	public static void initialize() {
		es = Executors.newFixedThreadPool(ArachneGlobals.parallelCount);
		for(int i = 1; i <= ArachneGlobals.parallelCount; i++) {
			final int j = i;
			Thread t = new Thread(new Runnable() {
				
				@Override
				public void run() {
					Thread.currentThread().setName("ArachneCrawlThread-"+j);
					while(ArachneGlobals.running) {
						try {
							Thread.sleep(0, 5000);
						} catch (InterruptedException e) {
							
						}
						crawlNext();
					}
				}
				
			});
			es.submit(t);
		}
	}
	
	public static void crawlNext() {
		lock.lock();
		Page p = CrawlerQueue.getNextPage();
		if(p==null||!p.getDomain().availableForCrawl()||p.isCrawlStarted()) {
			lock.unlock();
			return;
		}
		p.getDomain().updateLastCrawl();
		p.setCrawlStarted(true);
		lock.unlock();
		crawl(p);
	}
	
	public static void crawl(Page p) {
		Document doc = null;
		try {
			doc = Jsoup.connect("http://"+p.getUrl()).userAgent(ArachneGlobals.USER_AGENT+"/"+ArachneGlobals.ARACHNE_VER).followRedirects(true).timeout(5000).get();
		} catch (IOException e1) {
			p.setCrawled(true);
			p.setCrawlStarted(false);
			CrawlerQueue.removeQueuePage(p);
			return;
		}
		if(doc==null) {
			p.setCrawled(true);
			p.setCrawlStarted(false);
			CrawlerQueue.removeQueuePage(p);
			return;
		}
		if(!URLUtils.getDomainName(doc.baseUri()).endsWith(p.getDomain().getDomain())) {
			p.setCrawled(true);
			p.setCrawlStarted(false);
			CrawlerQueue.removeQueuePage(p);
			return;
		}
		try {p.setPageRawText(doc.body().text());} catch (NullPointerException e) {
			p.setCrawled(true);
			p.setCrawlStarted(false);
			CrawlerQueue.removeQueuePage(p);
		}
		try {p.setTitle(doc.select("title").size() > 0 ? doc.select("title").last().text() : "");} catch (NullPointerException e) {}
		try {p.setDesc(doc.select("meta[itemprop=description]").size() > 0 ? doc.select("meta[name=description]").last().text() : "");} catch (NullPointerException e) {}
		try {p.setContentType(doc.select("meta[http-equiv=Content-Type]").size() > 0 ? doc.select("meta[http-equiv=Content-Type]").last().text() : "");} catch (NullPointerException e) {}
		try {p.setFaviconUrl(doc.select("link[rel=icon][href]").size() > 0 ? doc.select("link[rel=icon][href]").last().attr("href") : "");} catch (NullPointerException e) {}
		keywordResolve(p, doc);
		linkResolve(p ,doc);
		imgResolve(p, doc);
		p.setUsingJavascript(doc.select("script").size() > 0);
		p.setUsingHtml5(hasHtml5Tags(doc));
		p.setViewportMeta(doc.select("meta[name=viewport]").size() > 0);
		p.setUsingTwitterExtras(doc.select("meta[name^=twitter]").size() > 0);
		p.setUsingOpengraphExtras(doc.select("meta[property^=og]").size() > 0);
		p.setUsingAppleExtras(doc.select("meta[name^=apple]").size() > 0 || doc.select("meta[rel^=apple]").size() > 0);
		p.setUsingMicrosoftExtras(doc.select("meta[name^=ms]").size()>0);
		p.setCrawled(true);
		for(String pg : p.getInternalLinks()) {
			try {
				if(p.isSchemaSecure()) {
					new Page((new URL(new URL("https://"+p.getUrl()), pg).toString()), false);
				} else {
					new Page((new URL(new URL("http://"+p.getUrl()), pg).toString()), false);
				}
			} catch (MalformedURLException e) {
				
			}
		}
		//CrawlerQueue.resetPageCursor();
	}
	
	private static void keywordResolve(Page p, Document d) {
		Map<String, Integer> keywords = p.getKeywords();
		for(String word : p.getPageRawText().split(" ")) {
			word = word.replaceAll("[^a-zA-Z]", "").toLowerCase();
			if(!(word.length()>0)) {
				continue;
			}
			if(keywords.containsKey(word)) {
				keywords.put(word, keywords.get(word)+1);
				continue;
			}
			keywords.put(word, 1);
		}
	}
	
	private static void linkResolve(Page p, Document d) {
		for(Element e : d.select("a[href]")) {
			String urlAbs = e.absUrl("href");
			String urlRel = e.attr("href");
			if(!urlRel.startsWith("http")) {
				if(!p.getInternalLinks().contains(urlRel)) {
					p.getInternalLinks().add(urlRel);
				}
				continue;
			} else if (!p.getOutgoingLinks().contains(urlRel)&&urlRel.length()>1) {
				p.getOutgoingLinks().add(urlRel);
				continue;
			}
		}
	}
	
	private static void imgResolve(Page p, Document d) {
		for(Element e : d.select("img[src]")) {
			String urlAbs = e.absUrl("src");
			if(!p.getImages().contains(urlAbs)) {
				p.getImages().add(urlAbs);
			}
		}
	}
	
	private static boolean hasHtml5Tags(Document d) {
		String[] tags = new String[] {"article","aside","audio","bdi","canvas","data","datalist","details","dialog","embed","figcaption","figure","footer","header",
				"main","mark","menuitem","meter","nav","output","picture","progress","rb","rt","ruby","section","source","summary","template","time","track","video","wbr"};
		for(String t : tags) {
			if(d.select(t).size()>0) {
				return true;
			}
		}
		return false;
	}

	public static ExecutorService getExecutorService() {
		return es;
	}

}
