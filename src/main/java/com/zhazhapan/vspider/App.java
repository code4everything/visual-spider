package com.zhazhapan.vspider;

import com.zhazhapan.util.Formatter;
import com.zhazhapan.util.ThreadPool;
import com.zhazhapan.vspider.controller.MainController;
import com.zhazhapan.vspider.modules.constant.DefaultConfigValues;
import com.zhazhapan.vspider.modules.constant.Values;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * @author pantao
 */
public class App extends Application {

    /**
     * 界面控制器
     */
    public static MainController mainController = null;
    /**
     * 爬虫控制器
     */
    public static VsController controller = new VsController();
    /**
     * 待爬取的URLs
     */
    public static String[] domains;
    /**
     * 记录访问过的URLs
     */
    public static ArrayList<String> visitUrls = new ArrayList<>();
    /**
     * 记录下载过的URLs
     */
    public static ArrayList<String> downloadUrls = new ArrayList<>();
    /**
     * 爬取延迟
     */
    public static int crawlingDelay = DefaultConfigValues.POLITENESS_DELAY;
    /**
     * 爬虫匹配（不匹配的链接将不会爬取，匹配的链接会进入访问状态）
     */
    public static Pattern crawlFilterPattern = Pattern.compile(".*");
    /**
     * 访问匹配（不匹配的链接将不会访问，匹配的链接会将服务器返回的源代码传送到下载模式）
     */
    public static Pattern visitFilterPattern = Pattern.compile(".*");
    /**
     * 下载匹配（从网页源代码获取可以下载的资源，资源链接不匹配的将不会下载）
     */
    public static Pattern downloadFilterPattern = Pattern.compile(".*");
    /**
     * 下载的存储目录
     */
    public static String DOWNLOAD_FOLDER = DefaultConfigValues.CRAWL_STORAGE_FOLDER + Values.SEPARATOR + "files" +
            Values.SEPARATOR + Formatter.datetimeToCustomString(new Date(), "yyyyMMdd");
    private static Logger logger = Logger.getLogger(App.class);

    /**
     * 主程序入口
     *
     * @param args {@link String}
     */
    public static void main(String[] args) {
        logger.info("start to run app");
        initThreadPool();
        // 启动JavaFX，会调用start方法
        launch(args);
    }

    /**
     * 初始化线程池
     */
    public static void initThreadPool() {
        ThreadPool.setCorePoolSize(1);
        ThreadPool.setMaximumPoolSize(5);
        ThreadPool.setKeepAliveTime(100);
        ThreadPool.setTimeUnit(TimeUnit.MILLISECONDS);
        ThreadPool.init();
    }

    @Override
    public void start(Stage stage) {
        try {
            BorderPane root = FXMLLoader.load(getClass().getResource("/view/MainWindow.fxml"));
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            logger.error("load fxml error: " + e.getMessage());
        }
        stage.setTitle(Values.MAIN_TITLE);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/image/spider.jpg")));
        stage.show();
        stage.setOnCloseRequest((WindowEvent event) -> {
            stage.setIconified(true);
            event.consume();
        });
    }
}
