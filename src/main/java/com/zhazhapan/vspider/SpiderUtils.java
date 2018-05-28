package com.zhazhapan.vspider;

import com.zhazhapan.modules.constant.ValueConsts;
import com.zhazhapan.util.Checker;
import com.zhazhapan.util.FileExecutor;
import com.zhazhapan.util.NetUtils;
import com.zhazhapan.util.Utils;
import com.zhazhapan.util.dialog.Alerts;
import com.zhazhapan.vspider.modules.constant.SpiderValueConsts;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * @author pantao
 * @since 2018/4/14
 */
public class SpiderUtils {

    private static final Logger LOGGER = Logger.getLogger(SpiderUtils.class);

    private SpiderUtils() {}

    /**
     * 从 html 源码中解析出 xpath 内容
     *
     * @param xpath 表达式
     * @param html 源码
     *
     * @return 解析结果
     */
    public static String evaluate(String xpath, String html) {
        try {
            return NetUtils.evaluate(xpath, html);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return ValueConsts.EMPTY_STRING;
        }
    }

    /**
     * 保存文件
     *
     * @param file 文件路径
     * @param content 内容
     * @param append 保存方式
     */
    public static void saveFile(String file, String content, boolean append) {
        try {
            if (Checker.isNotExists(file)) {
                FileExecutor.createFile(file);
            }
            FileExecutor.saveFile(file, content, append);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            Alerts.showError(SpiderValueConsts.MAIN_TITLE, e.getMessage());
        }
    }

    /**
     * 打开文件
     *
     * @param file 文件路径
     */
    public static void openFile(String file) {
        try {
            Utils.openFile(file);
        } catch (IOException e) {
            Alerts.showError(SpiderValueConsts.MAIN_TITLE, e.getMessage());
        }
    }
}
