package com.zhazhapan.vspider;

import com.zhazhapan.util.FileExecutor;
import com.zhazhapan.util.Utils;
import com.zhazhapan.util.dialog.Alerts;
import com.zhazhapan.vspider.modules.constant.Values;

import java.io.IOException;

/**
 * @author pantao
 * @since 2018/4/14
 */
public class SpiderUtils {

    private SpiderUtils() {}

    public static void saveFile(String file, String content, boolean append) {
        try {
            FileExecutor.saveFile(file, content, append);
        } catch (IOException e) {
            Alerts.showError(Values.MAIN_TITLE, e.getMessage());
        }
    }

    public static void openFile(String file) {
        try {
            Utils.openFile(file);
        } catch (IOException e) {
            Alerts.showError(Values.MAIN_TITLE, e.getMessage());
        }
    }
}
