package net.aionstudios.aosarachne.content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.aionstudios.aosarachne.crawling.CrawlerQueue;
import net.aionstudios.aosarachne.domain.Domain;
import net.aionstudios.aosarachne.domain.DomainManager;
import net.aionstudios.aosarachne.util.URLUtils;

public class Page {
	
	/*Crawling Variables*/
	private boolean crawled = false;
	private boolean crawlstarted = false;
	private boolean schemaSecure = true;
	
	/*Required*/
	private String url = "";
	private String urlBaseName = "";
	
	/*Standard*/
	private String title = "";
	private String desc = "";
	private String contentType = "";
	private String pageRawText = "";
	
	/*Conditional*/
	private List<String> outgoingLinks = new LinkedList<String>();
	private List<String> internalLinks = new LinkedList<String>();
	private List<String> images = new LinkedList<String>();
	private Map<String, Integer> keywords = new HashMap<String, Integer>();
	private String faviconUrl = "";
	
	/*Boolean*/
	private boolean usingJavascript = false;
	private boolean usingHtml5 = false;
	private boolean viewportMeta = false;
	
	/*Extended Meta*/
	private boolean usingTwitterExtras = false;
	private boolean usingOpengraphExtras = false;
	private boolean usingAppleExtras = false;
	private boolean usingMicrosoftExtras = false;
	
	private Domain d = null;
	
	public Page(String pageUrl, boolean createDomainIfNull) {
		url = !pageUrl.startsWith("http") ? URLUtils.stripRequestMethod("http://"+pageUrl) : URLUtils.stripRequestMethod(pageUrl);
		if(url==null) {
			return;
		}
		try {d = DomainManager.getDomain(url, createDomainIfNull);}catch(NullPointerException e) {}
		if(d==null) {
			return;
		}
		if(d.allowedCrawl(url)) {
			CrawlerQueue.addQueuePage(d, this, false);
		}
	}
	
	public boolean isCrawled() {
		return crawled;
	}
	public void setCrawled(boolean crawled) {
		CrawlerQueue.addCrawledPage(d, this);
		this.crawled = crawled;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUrlBaseName() {
		return urlBaseName;
	}
	public void setUrlBaseName(String urlBaseName) {
		this.urlBaseName = urlBaseName;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public List<String> getOutgoingLinks() {
		return outgoingLinks;
	}
	public List<String> getInternalLinks() {
		return internalLinks;
	}
	public List<String> getImages() {
		return images;
	}
	public String getFaviconUrl() {
		return faviconUrl;
	}
	public void setFaviconUrl(String faviconUrl) {
		this.faviconUrl = faviconUrl;
	}
	public boolean isUsingJavascript() {
		return usingJavascript;
	}
	public void setUsingJavascript(boolean usingJavascript) {
		this.usingJavascript = usingJavascript;
	}
	public boolean isUsingHtml5() {
		return usingHtml5;
	}
	public void setUsingHtml5(boolean usingHtml5) {
		this.usingHtml5 = usingHtml5;
	}
	public boolean isViewportMeta() {
		return viewportMeta;
	}
	public void setViewportMeta(boolean viewportMeta) {
		this.viewportMeta = viewportMeta;
	}
	public boolean isUsingTwitterExtras() {
		return usingTwitterExtras;
	}
	public void setUsingTwitterExtras(boolean usingTwitterExtras) {
		this.usingTwitterExtras = usingTwitterExtras;
	}
	public boolean isUsingOpengraphExtras() {
		return usingOpengraphExtras;
	}
	public void setUsingOpengraphExtras(boolean usingOpengraphExtras) {
		this.usingOpengraphExtras = usingOpengraphExtras;
	}
	public boolean isUsingAppleExtras() {
		return usingAppleExtras;
	}
	public void setUsingAppleExtras(boolean usingAppleExtras) {
		this.usingAppleExtras = usingAppleExtras;
	}
	public boolean isUsingMicrosoftExtras() {
		return usingMicrosoftExtras;
	}
	public void setUsingMicrosoftExtras(boolean usingMicrosoftExtras) {
		this.usingMicrosoftExtras = usingMicrosoftExtras;
	}
	public boolean isSchemaSecure() {
		return schemaSecure;
	}
	public void setSchemaSecure(boolean schemaSecure) {
		this.schemaSecure = schemaSecure;
	}
	public String getPageRawText() {
		return pageRawText;
	}
	public void setPageRawText(String pageRawText) {
		this.pageRawText = pageRawText;
	}
	public Map<String, Integer> getKeywords() {
		return keywords;
	}
	public Domain getDomain() {
		return d;
	}
	public boolean isCrawlStarted() {
		return crawlstarted;
	}
	public void setCrawlStarted(boolean crawlstarted) {
		this.crawlstarted = crawlstarted;
	}
	
}
