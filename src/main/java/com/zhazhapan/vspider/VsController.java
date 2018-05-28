package com.zhazhapan.vspider;

import com.zhazhapan.util.Checker;
import com.zhazhapan.util.Formatter;
import com.zhazhapan.vspider.modules.constant.DefaultConfigValues;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import org.apache.log4j.Logger;

/**
 * @author pantao
 */
public class VsController {

    /**
     * 是否已经初始化
     */
    public boolean isInited = false;

    private static final Logger LOGGER = Logger.getLogger(VsController.class);

    private CrawlController controller = null;

    private int numberOfCrawlers = DefaultConfigValues.NUMBER_OF_CRAWLERS;

    /**
     * 初始化，其他参数使用默认数据
     *
     * @param links {@link String}
     */
    public void init(String[] links) {
        init(DefaultConfigValues.NUMBER_OF_CRAWLERS, links);
    }

    /**
     * 初始化，其他参数使用默认数据
     *
     * @param numberOfCrawlers 爬虫线程数
     * @param links 待爬取链接
     */
    public void init(int numberOfCrawlers, String[] links) {
        init(numberOfCrawlers, DefaultConfigValues.POLITENESS_DELAY, links);
    }

    /**
     * 初始化，其他参数使用默认数据
     *
     * @param numberOfCrawlers 爬虫线程数
     * @param politenessDelay 延迟
     * @param links 待爬取链接
     */
    public void init(int numberOfCrawlers, int politenessDelay, String[] links) {
        init(numberOfCrawlers, DefaultConfigValues.MAX_DEPTH_OF_CRAWLING, Integer.MAX_VALUE, politenessDelay, links);
    }

    /**
     * 初始化，其他参数使用默认数据
     *
     * @param maxDepthOfCrawling 抓取深度
     * @param maxPagesToFetch 最大抓取页数
     * @param politenessDelay 延迟
     * @param links 待爬取链接
     */
    public void init(int maxDepthOfCrawling, int maxPagesToFetch, int politenessDelay, String[] links) {
        init(DefaultConfigValues.NUMBER_OF_CRAWLERS, maxDepthOfCrawling, maxPagesToFetch, politenessDelay, links);
    }

    /**
     * 初始化
     *
     * @param numberOfCrawlers 爬虫线程数
     * @param maxDepthOfCrawling 抓取深度
     * @param maxPagesToFetch 最大抓取页数
     * @param politenessDelay 延迟
     * @param links 待爬取链接
     */
    public void init(int numberOfCrawlers, int maxDepthOfCrawling, int maxPagesToFetch, int politenessDelay, String[]
            links) {
        this.numberOfCrawlers = numberOfCrawlers;
        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(DefaultConfigValues.CRAWL_STORAGE_FOLDER);
        config.setMaxDepthOfCrawling(maxDepthOfCrawling);
        config.setIncludeHttpsPages(true);
        config.setMaxPagesToFetch(maxPagesToFetch);
        config.setIncludeBinaryContentInCrawling(false);
        config.setPolitenessDelay(politenessDelay);
        config.setUserAgentString(DefaultConfigValues.USER_AGENT);
        config.setResumableCrawling(true);

        if (com.zhazhapan.vspider.models.CrawlConfig.getTurnOnProxy().get()) {
            LOGGER.info("open proxy");
            config.setProxyHost(com.zhazhapan.vspider.models.CrawlConfig.getProxyServer().get());
            config.setProxyPort(Formatter.stringToInt(com.zhazhapan.vspider.models.CrawlConfig.getProxyPort().get()));
            config.setProxyUsername(com.zhazhapan.vspider.models.CrawlConfig.getProxyUser().get());
            config.setProxyPassword(com.zhazhapan.vspider.models.CrawlConfig.getProxyPass().get());
        }

        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        robotstxtConfig.setEnabled(false);
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        try {
            controller = new CrawlController(config, pageFetcher, robotstxtServer);
            for (String link : links) {
                if (Checker.isHyperLink(link)) {
                    controller.addSeed(link);
                }
            }
            isInited = true;
        } catch (Exception e) {
            LOGGER.error("start to crawl urls error: " + e.getMessage());
        }
    }

    /**
     * 关闭爬虫，不可恢复
     */
    public void shutdown() {
        if (Checker.isNotNull(controller)) {
            controller.shutdown();
        }
    }

    /**
     * 启动爬虫
     *
     * @return 是否启动成功
     */
    public boolean start() {
        if (Checker.isNull(controller)) {
            return false;
        } else {
            controller.start(Crawler.class, numberOfCrawlers);
            return true;
        }
    }

    /**
     * 是否已经完成
     *
     * @return {@link Boolean}
     */
    public boolean isFinished() {
        return controller.isFinished();
    }
}
