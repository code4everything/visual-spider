package com.zhazhapan.vspider.modules.constant;

import com.zhazhapan.modules.constant.ValueConsts;
import com.zhazhapan.util.RandomUtils;

import java.io.File;

/**
 * @author pantao
 */
public class DefaultConfigValues {

    /**
     * 最大线程数
     */
    public static final int MAX_THREADS = 5;

    /**
     * 默认存储目录
     */
    public static final String CRAWL_STORAGE_FOLDER = ValueConsts.USER_HOME + SpiderValueConsts.SEPARATOR + "vspider";

    /**
     * SQL文件及路径
     */
    public static final String SQL_PATH = CRAWL_STORAGE_FOLDER + File.separator + RandomUtils
            .getRandomStringOnlyLowerCase(8) + ".sql";

    /**
     * 爬虫线程
     */
    public static final int NUMBER_OF_CRAWLERS = 3;

    /**
     * 延迟
     */
    public static final int POLITENESS_DELAY = 500;

    /**
     * user agent
     */
    public static final String USER_AGENT = "mozilla/5.0 (windows nt 10.0; win64; x64) applewebkit/537.36 (khtml, " +
            "like gecko) chrome/59.0.3071.115 safari/537.36";

    /**
     * 广度
     */
    public static final int MAX_DEPTH_OF_CRAWLING = 32767;

    /**
     * 深度
     */
    public static final int DEPTH_OF_CRAWLING = 99;
}
