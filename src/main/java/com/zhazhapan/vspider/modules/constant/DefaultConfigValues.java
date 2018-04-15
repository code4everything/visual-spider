/**
 *
 */
package com.zhazhapan.vspider.modules.constant;

import com.zhazhapan.modules.constant.ValueConsts;
import com.zhazhapan.util.RandomUtils;

import java.io.File;

/**
 * @author pantao
 */
public class DefaultConfigValues {

    public static final String CRAWL_STORAGE_FOLDER = ValueConsts.USER_HOME + SpiderValueConsts.SEPARATOR + "vspider";

    public static final String SQL_PATH = CRAWL_STORAGE_FOLDER + File.separator + RandomUtils
            .getRandomStringOnlyLowerCase(8) + ".sql";

    public static final int NUMBER_OF_CRAWLERS = 3;

    public static final int POLITENESS_DELAY = 500;

    public static final String USER_AGENT = "mozilla/5.0 (windows nt 10.0; win64; x64) applewebkit/537.36 (khtml, " +
            "like gecko) chrome/59.0.3071.115 safari/537.36";

    public static final int MAX_DEPTH_OF_CRAWLING = 32767;

    public static final int DEPTH_OF_CRAWLING = 99;
}
