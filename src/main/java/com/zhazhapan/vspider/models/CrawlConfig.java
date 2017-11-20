/**
 * 
 */
package com.zhazhapan.vspider.models;

import com.zhazhapan.vspider.modules.constant.DefaultConfigValues;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * @author pantao
 *
 */
@SuppressWarnings("restriction")
public class CrawlConfig {

	private static SimpleBooleanProperty crawlImages = new SimpleBooleanProperty(true);

	private static SimpleBooleanProperty crawlVideos = new SimpleBooleanProperty(false);

	private static SimpleBooleanProperty crawlDocs = new SimpleBooleanProperty(false);

	private static SimpleBooleanProperty crawlLinks = new SimpleBooleanProperty(false);

	private static SimpleBooleanProperty crawlOthers = new SimpleBooleanProperty(false);

	private static SimpleBooleanProperty repeatCrawl = new SimpleBooleanProperty(false);

	private static SimpleStringProperty numberOfCrawlers = new SimpleStringProperty(
			String.valueOf(DefaultConfigValues.NUMBER_OF_CRAWLERS));

	private static SimpleStringProperty maxDepthOfCrawling = new SimpleStringProperty(
			String.valueOf(DefaultConfigValues.DEPTH_OF_CRAWLING));

	private static SimpleStringProperty maxPagesToFetch = new SimpleStringProperty(String.valueOf(Integer.MAX_VALUE));

	private static SimpleStringProperty politenessDelay = new SimpleStringProperty(
			String.valueOf(DefaultConfigValues.POLITENESS_DELAY));

	private static SimpleBooleanProperty turnOnProxy = new SimpleBooleanProperty(false);

	private static SimpleStringProperty proxyServer = new SimpleStringProperty();

	private static SimpleStringProperty proxyPort = new SimpleStringProperty();

	private static SimpleStringProperty proxyUser = new SimpleStringProperty();

	private static SimpleStringProperty proxyPass = new SimpleStringProperty();

	public static SimpleBooleanProperty getRepeatCrawl() {
		return repeatCrawl;
	}

	public static void setRepeatCrawl(SimpleBooleanProperty repeatCrawl) {
		CrawlConfig.repeatCrawl = repeatCrawl;
	}

	public static SimpleBooleanProperty getCrawlImages() {
		return crawlImages;
	}

	public static void setCrawlImages(SimpleBooleanProperty crawlImages) {
		CrawlConfig.crawlImages = crawlImages;
	}

	public static SimpleBooleanProperty getCrawlVideos() {
		return crawlVideos;
	}

	public static void setCrawlVideos(SimpleBooleanProperty crawlVideos) {
		CrawlConfig.crawlVideos = crawlVideos;
	}

	public static SimpleBooleanProperty getCrawlDocs() {
		return crawlDocs;
	}

	public static void setCrawlDocs(SimpleBooleanProperty crawlDocs) {
		CrawlConfig.crawlDocs = crawlDocs;
	}

	public static SimpleBooleanProperty getCrawlLinks() {
		return crawlLinks;
	}

	public static void setCrawlLinks(SimpleBooleanProperty crawlLinks) {
		CrawlConfig.crawlLinks = crawlLinks;
	}

	public static SimpleBooleanProperty getCrawlOthers() {
		return crawlOthers;
	}

	public static void setCrawlOthers(SimpleBooleanProperty crawlOthers) {
		CrawlConfig.crawlOthers = crawlOthers;
	}

	public static SimpleStringProperty getNumberOfCrawlers() {
		return numberOfCrawlers;
	}

	public static void setNumberOfCrawlers(SimpleStringProperty numberOfCrawlers) {
		CrawlConfig.numberOfCrawlers = numberOfCrawlers;
	}

	public static SimpleStringProperty getMaxDepthOfCrawling() {
		return maxDepthOfCrawling;
	}

	public static void setMaxDepthOfCrawling(SimpleStringProperty maxDepthOfCrawling) {
		CrawlConfig.maxDepthOfCrawling = maxDepthOfCrawling;
	}

	public static SimpleStringProperty getMaxPagesToFetch() {
		return maxPagesToFetch;
	}

	public static void setMaxPagesToFetch(SimpleStringProperty maxPagesToFetch) {
		CrawlConfig.maxPagesToFetch = maxPagesToFetch;
	}

	public static SimpleStringProperty getPolitenessDelay() {
		return politenessDelay;
	}

	public static void setPolitenessDelay(SimpleStringProperty politenessDelay) {
		CrawlConfig.politenessDelay = politenessDelay;
	}

	public static SimpleBooleanProperty getTurnOnProxy() {
		return turnOnProxy;
	}

	public static void setTurnOnProxy(SimpleBooleanProperty turnOnProxy) {
		CrawlConfig.turnOnProxy = turnOnProxy;
	}

	public static SimpleStringProperty getProxyServer() {
		return proxyServer;
	}

	public static void setProxyServer(SimpleStringProperty proxyServer) {
		CrawlConfig.proxyServer = proxyServer;
	}

	public static SimpleStringProperty getProxyPort() {
		return proxyPort;
	}

	public static void setProxyPort(SimpleStringProperty proxyPort) {
		CrawlConfig.proxyPort = proxyPort;
	}

	public static SimpleStringProperty getProxyUser() {
		return proxyUser;
	}

	public static void setProxyUser(SimpleStringProperty proxyUser) {
		CrawlConfig.proxyUser = proxyUser;
	}

	public static SimpleStringProperty getProxyPass() {
		return proxyPass;
	}

	public static void setProxyPass(SimpleStringProperty proxyPass) {
		CrawlConfig.proxyPass = proxyPass;
	}
}
