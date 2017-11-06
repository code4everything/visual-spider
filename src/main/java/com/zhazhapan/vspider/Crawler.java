/**
 * 
 */
package com.zhazhapan.vspider;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.zhazhapan.util.Checker;
import com.zhazhapan.util.Downloader;
import com.zhazhapan.vspider.models.CrawlConfig;
import com.zhazhapan.vspider.modules.constant.Values;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import javafx.application.Platform;

/**
 * @author pantao
 *
 */
public class Crawler extends WebCrawler {

	// private final Pattern FILTER_PATTERN =
	// Pattern.compile(".*\\.(js|css)(\\?.*)?$", Pattern.CASE_INSENSITIVE);

	public final Pattern IMAGES_PATTERN = Pattern.compile(
			"(https?:)?//[^\\s&;\"':<>]*?\\.(bmp|gif|jpe?g|png|tiff?|pcx|tga|svg|pic)(\\?[^?\\s\"':<>]*)?",
			Pattern.CASE_INSENSITIVE);

	private final Pattern VIDEOS_PATTERN = Pattern.compile(
			"(https?:)?//[^\\s&;\"':<>]*\\.(avi|mov|swf|asf|navi|wmv|3gp|mkv|flv|rm(vb)?|webm|mpg|mp4|qsv|mpe?g|mp3|aac|ogg|wav|flac|ape|wma|aif|au|ram|mmf|amr|flac)(\\?[^?\\s\"':<>]*)?",
			Pattern.CASE_INSENSITIVE);

	private final Pattern DOCS_PATTERN = Pattern.compile(
			"(https?:)?//[^\\s&;\"':<>]*\\.(pdf|docx?|txt|log|conf|java|xml|json|css|js|html|hml|php|wps|rtf)(\\?[^?\\s\"':<>]*)?",
			Pattern.CASE_INSENSITIVE);

	private final Pattern OTHERS_PATTERN = Pattern.compile(
			"(https?:)?//[^\\s&;\"':<>]*\\.(zip|[0-9a-z]?z|exe|dmg|iso|jar|msi|rar|tmp|xlsx?|mdf|com|c|asm|for|lib|lst|msg|obj|pas|wki|bas|map|bak|dot|bat|sh|rpm)(\\?[^?\\s\"':<>]*)?",
			Pattern.CASE_INSENSITIVE);

	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String urlStr = url.getURL();
		if (!App.visitUrls.contains(urlStr)) {
			for (String domain : App.domains) {
				if (urlStr.contains(domain)) {
					App.visitUrls.add(urlStr);
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void visit(Page page) {
		String url = page.getWebURL().getURL();
		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			Platform.runLater(() -> {
				App.mainController.stautsLabel.setText("validating url: " + url);
				App.mainController.htmlContent.appendText("crawling url: " + url + "\r\n");
			});
			downloadURL(url, htmlParseData.getHtml());
		}
	}

	public void downloadURL(String url, String html) {
		Matcher matcher;
		if (CrawlConfig.getCrawlImages().get()) {
			matcher = IMAGES_PATTERN.matcher(html);
			addURLs("image", matcher);
		}
		if (CrawlConfig.getCrawlVideos().get()) {
			matcher = VIDEOS_PATTERN.matcher(html);
			addURLs("media", matcher);
		}
		if (CrawlConfig.getCrawlDocs().get()) {
			matcher = DOCS_PATTERN.matcher(html);
			addURLs("document", matcher);
		}
		if (CrawlConfig.getCrawlOthers().get()) {
			matcher = OTHERS_PATTERN.matcher(html);
			addURLs("others", matcher);
		}
		if (Checker.isNotEmpty(url) && CrawlConfig.getCrawlLinks().get()) {
			String path = App.DOWNLOAD_FOLDER + Values.SEPARATOR + "link";
			Downloader.download(path, (url.startsWith("//") ? "http:" : "") + url);
		}
	}

	public void addURLs(String path, Matcher matcher) {
		path = App.DOWNLOAD_FOLDER + Values.SEPARATOR + path;
		while (matcher.find()) {
			String url = matcher.group();
			download(path, url);
		}
	}

	public void download(String path, String url) {
		String realUrl = url.split("\\?")[0];
		if (!App.downloadUrls.contains(realUrl) && App.filterPatter.matcher(url).find()) {
			App.downloadUrls.add(realUrl);
			Platform.runLater(() -> App.mainController.logOut.appendText("downloading url: " + url + "\r\n"));
			path += Values.SEPARATOR + url.substring(url.lastIndexOf(".") + 1);
			if (path.contains(Values.QUESTION_MARK)) {
				path = path.substring(0, path.indexOf(Values.QUESTION_MARK));
			}
			Downloader.download(path, (url.startsWith("//") ? "http:" : "") + url);
			try {
				Thread.sleep(App.crawlingDelay);
			} catch (InterruptedException e) {
				logger.error("thread sleep error: " + e.getMessage());
			}
		}
	}
}
