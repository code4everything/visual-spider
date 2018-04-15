package com.zhazhapan.vspider;

import com.zhazhapan.util.Checker;
import com.zhazhapan.util.Downloader;
import com.zhazhapan.vspider.models.CrawlConfig;
import com.zhazhapan.vspider.models.MysqlConfig;
import com.zhazhapan.vspider.modules.constant.DefaultConfigValues;
import com.zhazhapan.vspider.modules.constant.SpiderValueConsts;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import javafx.application.Platform;
import javafx.util.Pair;

import java.net.URL;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author pantao
 */
public class Crawler extends WebCrawler {

    // private final Pattern FILTER_PATTERN =
    // Pattern.compile(".*\\.(js|css)(\\?.*)?$", Pattern.CASE_INSENSITIVE);

    /**
     * 匹配图片
     */
    public final Pattern IMAGES_PATTERN = Pattern.compile("(https?:)?//[^\\s&;\"':<>]*?\\." + "" + "" + "" + "" + ""
            + "(bmp|gif|jpe?g|png|tiff?|pcx|tga|svg|pic)(\\?[^?\\s\"':<>]*)?", Pattern.CASE_INSENSITIVE);

    /**
     * 匹配媒体文件
     */
    private final Pattern VIDEOS_PATTERN = Pattern.compile("(https?:)?//[^\\s&;\"':<>]*\\." + "" + "" + "" + "" + ""
            + "(avi|mov|swf|asf|navi|wmv|3gp|mkv|flv|rm(vb)" +
            "?|webm|mpg|mp4|qsv|mpe?g|mp3|aac|ogg|wav|flac|ape|wma|aif|au|ram|mmf|amr|flac)(\\?[^?\\s\"':<>]*)?",
            Pattern.CASE_INSENSITIVE);

    /**
     * 匹配文档
     */
    private final Pattern DOCS_PATTERN = Pattern.compile("(https?:)?//[^\\s&;\"':<>]*\\." + "" + "" + "" + "" + "" +
            "(pdf|docx?|txt|log|conf|java|xml|json|css|js|html|hml|php|wps|rtf)(\\?[^?\\s\"':<>]*)?", Pattern
            .CASE_INSENSITIVE);

    /**
     * 匹配其他文件
     */
    private final Pattern OTHERS_PATTERN = Pattern.compile("(https?:)?//[^\\s&;\"':<>]*\\." + "" + "" + "" + "" + ""
            + "(zip|[0-9a-z]?z|exe|dmg|iso|jar|msi|rar|tmp|xlsx?|mdf|com|c|asm|for|lib|lst|msg|obj|pas|wki|bas|map" +
            "|bak" + "|dot|bat|sh|rpm)(\\?[^?\\s\"':<>]*)?", Pattern.CASE_INSENSITIVE);

    /**
     * 由crawler4j调用，前置（爬虫）过滤将在这里进行匹配
     */
    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String urlStr = url.getURL();
        if (SpiderApplication.crawlFilterPattern.matcher(urlStr).find() && !SpiderApplication.visitUrls.contains
                (urlStr)) {
            for (String domain : SpiderApplication.domains) {
                if (urlStr.contains(domain)) {
                    SpiderApplication.visitUrls.add(urlStr);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 由crawler4j调用，链接（访问）过滤将在这里进行匹配
     */
    @Override
    public void visit(Page page) {
        String url = page.getWebURL().getURL();
        if (SpiderApplication.visitFilterPattern.matcher(url).find() && page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            Platform.runLater(() -> {
                SpiderApplication.mainController.statusLabel.setText("validating url: " + url);
                SpiderApplication.mainController.htmlContent.appendText(SpiderValueConsts.VISITING_TIP + url + "\r\n");
            });
            downloadURL(url, htmlParseData.getHtml());
        }
    }

    /**
     * 判断需要下载的资源
     *
     * @param url {@link URL}
     * @param html {@link String}
     */
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
            String path = SpiderApplication.DOWNLOAD_FOLDER + SpiderValueConsts.SEPARATOR + "link";
            Downloader.download(path, (url.startsWith("//") ? "http:" : "") + url);
        }
        if (MysqlConfig.isEnableCustom()) {
            StringBuilder preSlice = new StringBuilder("insert into " + MysqlConfig.getTableName() + "(");
            StringBuilder postSlice = new StringBuilder(" values(");
            if (Checker.isNotEmpty(MysqlConfig.getFields())) {
                for (Pair<String, String> pair : MysqlConfig.getFields()) {
                    preSlice.append(pair.getValue()).append(",");
                    postSlice.append(SpiderUtils.evaluate(pair.getKey(), html)).append(",");
                }
                String pre = preSlice.toString();
                String post = postSlice.toString();
                String sql = pre.substring(0, pre.length() - 1) + ")" + post.substring(0, post.length() - 1) + ")";
                if (MysqlConfig.isEnableSql()) {
                    SpiderUtils.saveFile(DefaultConfigValues.SQL_PATH, sql + "\r\n", true);
                }
                try {
                    SpiderApplication.statement.executeUpdate(sql);
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    /**
     * 从源代码提取可下载的资源
     *
     * @param path {@link String}
     * @param matcher {@link Matcher}
     */
    public void addURLs(String path, Matcher matcher) {
        path = SpiderApplication.DOWNLOAD_FOLDER + SpiderValueConsts.SEPARATOR + path;
        while (matcher.find()) {
            String url = matcher.group();
            download(path, url);
        }
    }

    /**
     * 下载资源文件，并将链接添加到下载记录中，下载过滤将这里进行匹配
     *
     * @param path {@link String}
     * @param url {@link URL}
     */
    public void download(String path, String url) {
        String realUrl = url.split("\\?")[0];
        if (SpiderApplication.downloadFilterPattern.matcher(url).find() && !SpiderApplication.downloadUrls.contains
                (realUrl)) {
            SpiderApplication.downloadUrls.add(realUrl);
            Platform.runLater(() -> SpiderApplication.mainController.logOut.appendText(SpiderValueConsts
                    .DOWNLOADING_TIP + url + "\r\n"));
            path += SpiderValueConsts.SEPARATOR + url.substring(url.lastIndexOf(".") + 1);
            if (path.contains(SpiderValueConsts.QUESTION_MARK)) {
                path = path.substring(0, path.indexOf(SpiderValueConsts.QUESTION_MARK));
            }
            Downloader.download(path, (url.startsWith("//") ? "http:" : "") + url);
            try {
                Thread.sleep(SpiderApplication.crawlingDelay);
            } catch (InterruptedException e) {
                logger.error("thread sleep error: " + e.getMessage());
            }
        }
    }
}
