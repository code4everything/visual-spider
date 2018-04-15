package com.zhazhapan.vspider;

import com.zhazhapan.modules.constant.ValueConsts;
import com.zhazhapan.util.FileExecutor;
import com.zhazhapan.util.NetUtils;
import com.zhazhapan.util.Utils;
import com.zhazhapan.util.dialog.Alerts;
import com.zhazhapan.vspider.modules.constant.SpiderValueConsts;

import java.io.IOException;

/**
 * @author pantao
 * @since 2018/4/14
 */
public class SpiderUtils {

    private SpiderUtils() {}

    public static String evaluate(String xpath, String html) {
        try {
            return NetUtils.evaluate(xpath, html);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ValueConsts.EMPTY_STRING;
        }
    }

    public static void saveFile(String file, String content, boolean append) {
        try {
            FileExecutor.saveFile(file, content, append);
        } catch (IOException e) {
            Alerts.showError(SpiderValueConsts.MAIN_TITLE, e.getMessage());
        }
    }

    public static void openFile(String file) {
        try {
            Utils.openFile(file);
        } catch (IOException e) {
            Alerts.showError(SpiderValueConsts.MAIN_TITLE, e.getMessage());
        }
    }
}
