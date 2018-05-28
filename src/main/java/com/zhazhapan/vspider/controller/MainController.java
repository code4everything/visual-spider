package com.zhazhapan.vspider.controller;

import com.zhazhapan.modules.constant.ValueConsts;
import com.zhazhapan.util.Checker;
import com.zhazhapan.util.Formatter;
import com.zhazhapan.util.ThreadPool;
import com.zhazhapan.util.dialog.Alerts;
import com.zhazhapan.vspider.Crawler;
import com.zhazhapan.vspider.SpiderApplication;
import com.zhazhapan.vspider.SpiderUtils;
import com.zhazhapan.vspider.VsController;
import com.zhazhapan.vspider.models.CrawlConfig;
import com.zhazhapan.vspider.models.MysqlConfig;
import com.zhazhapan.vspider.modules.constant.DefaultConfigValues;
import com.zhazhapan.vspider.modules.constant.SpiderValueConsts;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.util.Pair;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;

import java.io.File;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * @author pantao
 */
public class MainController {

    private static final Logger LOGGER = Logger.getLogger(MainController.class);

    private static MainController mainController = null;

    /**
     * 下载记录
     */
    @FXML
    public TextArea logOut;

    /**
     * 链接检证状态标签
     */
    @FXML
    public Label statusLabel;

    /**
     * 访问记录
     */
    @FXML
    public TextArea htmlContent;

    @FXML
    public CheckBox customCK;

    @FXML
    private TextField crawlUrl;

    @FXML
    private CheckBox pictureCK;

    @FXML
    private CheckBox videoCK;

    @FXML
    private CheckBox linkCK;

    @FXML
    private CheckBox docCK;

    @FXML
    private CheckBox othersCK;

    @FXML
    private Button toggleCrawling;

    @FXML
    private TextField numsTF;

    @FXML
    private TextField depthTF;

    @FXML
    private TextField pagesTF;

    @FXML
    private TextField delayTF;

    @FXML
    private CheckBox proxyCK;

    @FXML
    private TextField proxyServerTF;

    @FXML
    private TextField proxyPortTF;

    @FXML
    private TextField proxyUserTF;

    @FXML

    private PasswordField proxyPassPF;

    @FXML
    private TextField crawlFilterTF;

    @FXML
    private TextField downloadFilterTF;

    @FXML
    private CheckBox repeatCK;

    @FXML
    private TextField visitFilterTF;

    private boolean crawling = false;

    /**
     * 获取唯一实例
     *
     * @return {@link MainController}
     */
    public static MainController getInstance() {
        return mainController;
    }

    /**
     * 初始化，由JavaFX调用
     */
    @FXML
    private void initialize() {
        LOGGER.info("init main window");
        // 创建唯一实例
        mainController = this;
        SpiderApplication.mainController = mainController;

        // 绑定模型数据
        pictureCK.selectedProperty().bindBidirectional(CrawlConfig.getCrawlImages());
        videoCK.selectedProperty().bindBidirectional(CrawlConfig.getCrawlVideos());
        linkCK.selectedProperty().bindBidirectional(CrawlConfig.getCrawlLinks());
        docCK.selectedProperty().bindBidirectional(CrawlConfig.getCrawlDocs());
        othersCK.selectedProperty().bindBidirectional(CrawlConfig.getCrawlOthers());

        numsTF.textProperty().bindBidirectional(CrawlConfig.getNumberOfCrawlers());
        depthTF.textProperty().bindBidirectional(CrawlConfig.getMaxDepthOfCrawling());
        pagesTF.textProperty().bindBidirectional(CrawlConfig.getMaxPagesToFetch());
        delayTF.textProperty().bindBidirectional(CrawlConfig.getPolitenessDelay());

        proxyCK.selectedProperty().bindBidirectional(CrawlConfig.getTurnOnProxy());
        proxyServerTF.textProperty().bindBidirectional(CrawlConfig.getProxyServer());
        proxyPortTF.textProperty().bindBidirectional(CrawlConfig.getProxyPort());
        proxyUserTF.textProperty().bindBidirectional(CrawlConfig.getProxyUser());
        proxyPassPF.textProperty().bindBidirectional(CrawlConfig.getProxyPass());

        repeatCK.selectedProperty().bindBidirectional(CrawlConfig.getRepeatCrawl());

        // 添加值改变事件
        downloadFilterTF.textProperty().addListener((ob, oldValue, newValue) -> {
            try {
                SpiderApplication.downloadFilterPattern = Pattern.compile(newValue, Pattern.CASE_INSENSITIVE);
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
        });

        crawlFilterTF.textProperty().addListener((ob, oldValue, newValue) -> {
            try {
                SpiderApplication.crawlFilterPattern = Pattern.compile(newValue, Pattern.CASE_INSENSITIVE);
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
        });

        visitFilterTF.textProperty().addListener((ob, oldValue, newValue) -> {
            try {
                SpiderApplication.visitFilterPattern = Pattern.compile(newValue, Pattern.CASE_INSENSITIVE);
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
        });
    }

    /**
     * 退出程序
     */
    public void exit() {
        if (crawling) {
            Optional<ButtonType> result = Alerts.showConfirmation(SpiderValueConsts.MAIN_TITLE, SpiderValueConsts
                    .EXIT_CRAWLING);
            if (result.get() != ButtonType.OK) {
                return;
            }
        }
        saveLog();
        // 关闭数据库连接
        try {
            if (Checker.isNotNull(SpiderApplication.statement)) {
                SpiderApplication.statement.close();
            }
            if (Checker.isNotNull(SpiderApplication.connection)) {
                SpiderApplication.connection.close();
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }
        System.exit(0);
    }

    /**
     * 保存爬取日志
     */
    public void saveLog() {
        String visitingLog = htmlContent.getText();
        if (Checker.isNotEmpty(visitingLog) && visitingLog.contains(SpiderValueConsts.VISITING_TIP)) {
            SpiderUtils.saveFile(SpiderApplication.DOWNLOAD_FOLDER + SpiderValueConsts.SEPARATOR + "visiting.log",
                    visitingLog, ValueConsts.TRUE);
        }
        String downloadingLog = logOut.getText();
        if (Checker.isNotEmpty(downloadingLog) && downloadingLog.contains(SpiderValueConsts.DOWNLOADING_TIP)) {
            SpiderUtils.saveFile(SpiderApplication.DOWNLOAD_FOLDER + SpiderValueConsts.SEPARATOR + "downloading.log",
                    downloadingLog, ValueConsts.TRUE);
        }
    }

    /**
     * 爬取URL
     */
    public void toCrawl() {
        if (crawling) {
            // 暂停爬虫
            crawling = false;
            toggleCrawling.setText(SpiderValueConsts.CRAWLER_START);
            statusLabel.setText("crawler suspend");
            SpiderApplication.controller.shutdown();
        } else {
            if (MysqlConfig.isEnableCustom() && !MysqlConfig.isConnectionSuccessful()) {
                MysqlConfig.setEnableSql(true);
                Alerts.showWarning(SpiderValueConsts.MAIN_TITLE, "数据库连接失败，将自动为您生成SQL文件");
            }
            // 开始爬虫
            if (!Checker.isHyperLink(crawlUrl.getText())) {
                String html = htmlContent.getText();
                if (Checker.isNotEmpty(html)) {
                    // 如果没有输入要爬取的链接，并且链接访问文本域的内容不为空，将直接把该内容作为源代码传送到下载模式
                    ThreadPool.executor.submit(() -> new Crawler().downloadURL("", html));
                    htmlContent.clear();
                }
                return;
            }
            crawling = true;
            toggleCrawling.setText(SpiderValueConsts.CRAWLER_STOP);
            LOGGER.info("start to crawl urls: " + crawlUrl.getText());
            statusLabel.setText("starting......");
            // 读取爬虫配置
            String[] urls = crawlUrl.getText().split(" ");
            SpiderApplication.domains = new String[urls.length];
            for (int i = 0; i < urls.length; i++) {
                String url = urls[i].replaceAll("https?://", "");
                if (url.contains("/")) {
                    SpiderApplication.domains[i] = url.substring(0, url.indexOf("/") + 1);
                } else {
                    SpiderApplication.domains[i] = url;
                }
            }
            // 爬虫参数转换为合法的数据
            int numNon = Formatter.stringToInt(CrawlConfig.getNumberOfCrawlers().get());
            int num = numNon < 1 ? DefaultConfigValues.NUMBER_OF_CRAWLERS : numNon;
            int depNon = Formatter.stringToInt(CrawlConfig.getMaxDepthOfCrawling().get());
            int dep = depNon < 1 || depNon > DefaultConfigValues.MAX_DEPTH_OF_CRAWLING ? DefaultConfigValues
                    .MAX_DEPTH_OF_CRAWLING : depNon;
            int pagNon = Formatter.stringToInt(CrawlConfig.getMaxPagesToFetch().get());
            int pag = pagNon < 1 ? Integer.MAX_VALUE : pagNon;
            int delNon = Formatter.stringToInt(CrawlConfig.getPolitenessDelay().get());
            int del = delNon < 1 ? DefaultConfigValues.POLITENESS_DELAY : delNon;
            SpiderApplication.crawlingDelay = del;
            ThreadPool.executor.submit(() -> {
                // 开启爬虫
                SpiderApplication.controller.init(num, dep, pag, del, urls);
                boolean res = SpiderApplication.controller.start();
                finished(res);
            });
        }
    }

    /**
     * 爬取完成时
     *
     * @param start 爬虫是否启动成功
     */
    private void finished(boolean start) {
        Platform.runLater(() -> {
            if (start) {
                statusLabel.setText("finished");
            } else {
                statusLabel.setText("start error");
            }
            crawling = false;
            toggleCrawling.setText(SpiderValueConsts.CRAWLER_START);
            if (CrawlConfig.getRepeatCrawl().get()) {
                repeatCrawl();
            }
        });
    }

    /**
     * 重复爬取，将不会启动crawler4j，而是直接从访问记录读取链接并重复访问
     */
    private void repeatCrawl() {
        int threads = Formatter.stringToInt(CrawlConfig.getNumberOfCrawlers().get());
        if (threads > DefaultConfigValues.MAX_THREADS) {
            threads = DefaultConfigValues.MAX_THREADS;
        }
        String[] urls = htmlContent.getText().replaceAll(SpiderValueConsts.VISITING_TIP, ValueConsts.EMPTY_STRING)
                .split("\n");
        Crawler crawler = new Crawler();
        for (int i = 0; i < threads; i++) {
            ThreadPool.executor.submit(() -> {
                while (CrawlConfig.getRepeatCrawl().get()) {
                    for (String url : urls) {
                        try {
                            crawler.downloadURL(url, Jsoup.connect(url).execute().body());
                            LOGGER.info("repeat visiting url: " + url);
                            Platform.runLater(() -> statusLabel.setText("validating url: " + url));
                        } catch (IOException e) {
                            LOGGER.error("something wrong when repeat crawler, message: " + e.getMessage());
                        }
                    }
                    try {
                        Thread.sleep(SpiderApplication.crawlingDelay);
                    } catch (InterruptedException e) {
                        LOGGER.error("sleep thread error: " + e.getMessage());
                    }
                }
            });
        }
    }

    /**
     * 重置爬虫，将删除爬虫记录
     */
    public void reset() {
        deleteFrontier();
        SpiderApplication.controller.shutdown();
        ThreadPool.executor.shutdownNow();
        saveLog();
        htmlContent.clear();
        logOut.clear();
        crawlUrl.clear();
        crawling = false;
        toggleCrawling.setText(SpiderValueConsts.CRAWLER_START);
        SpiderApplication.visitUrls.clear();
        SpiderApplication.downloadUrls.clear();
        SpiderApplication.initThreadPool();
        SpiderApplication.controller = new VsController();
    }

    /**
     * 删除crawler4j的配置文件
     */
    private void deleteFrontier() {
        deleteFile(new File(DefaultConfigValues.CRAWL_STORAGE_FOLDER + SpiderValueConsts.SEPARATOR + "frontier"));
    }

    /**
     * 删除文件夹或文件
     *
     * @param file {@link File}
     */
    public void deleteFile(File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                String[] children = file.list();
                if (Checker.isNotNull(children)) {
                    for (String aChildren : children) {
                        deleteFile(new File(file, aChildren));
                    }
                }
            }
            file.delete();
        }
    }

    /**
     * 打开存储目录
     */
    public void openStorageFolder() {
        SpiderUtils.openFile(DefaultConfigValues.CRAWL_STORAGE_FOLDER);
    }

    /**
     * 回车确认访问链接
     *
     * @param event {@link KeyEvent}
     */
    public void urlEnter(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            toCrawl();
        }
    }

    /**
     * 打开自定义爬取面板
     */
    public void customCrawling() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(SpiderValueConsts.MAIN_TITLE);
        dialog.setHeaderText(null);
        dialog.initModality(Modality.APPLICATION_MODAL);
        ButtonType ok = new ButtonType("确定", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("取消", ButtonBar.ButtonData.CANCEL_CLOSE);
        try {
            DialogPane dialogPane = FXMLLoader.load(getClass().getResource("/view/CustomCrawling.fxml"));
            dialogPane.getButtonTypes().addAll(ok, cancel);
            dialog.setDialogPane(dialogPane);
            Optional<ButtonType> result = dialog.showAndWait();
            if (ok.equals(result.get())) {
                //设置配置信息
                CustomCrawlingController controller = SpiderApplication.customCrawlingController;
                if (Checker.isNotNull(controller)) {
                    String mappings = controller.mappings.getText();
                    boolean enableCustom = controller.enableCustomCrawling.isSelected();
                    if (enableCustom && Checker.isNotEmpty(mappings)) {
                        MysqlConfig.setDbCondition(controller.dbCondition.getText());
                        MysqlConfig.setDbHost(controller.dbHost.getText());
                        MysqlConfig.setDbName(controller.dbName.getText());
                        MysqlConfig.setDbPassword(controller.dbPassword.getText());
                        MysqlConfig.setDbPort(controller.dbPort.getText());
                        MysqlConfig.setDbUsername(controller.dbUsername.getText());
                        MysqlConfig.setTableName(controller.dbTable.getText());
                        MysqlConfig.setEnableCustom(true);
                        MysqlConfig.setEnableSql(controller.enableSql.isSelected());
                        String[] mapping = mappings.split(ValueConsts.COMMA_SIGN);
                        MysqlConfig.getFields().clear();
                        for (String s : mapping) {
                            String[] keyValue = s.split("->");
                            Pair<String, String> map = new Pair<>(keyValue[0].trim(), keyValue[1].trim());
                            MysqlConfig.getFields().add(map);
                        }
                        connectDatabase();
                    } else {
                        MysqlConfig.setEnableCustom(false);
                        if (enableCustom) {
                            Alerts.showWarning(SpiderValueConsts.MAIN_TITLE, "映射关系为空，无法开启自定义爬取");
                        }
                    }
                }
            }
        } catch (IOException e) {
            Alerts.showError(SpiderValueConsts.MAIN_TITLE, e.getMessage());
        }
        customCK.setSelected(MysqlConfig.isEnableCustom());
    }

    /**
     * 连接到数据库
     */
    private void connectDatabase() {
        try {
            if (Checker.isNotNull(SpiderApplication.connection) && !SpiderApplication.connection.isClosed()) {
                SpiderApplication.connection.close();
            }
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://" + MysqlConfig.getDbHost() + ":" + MysqlConfig.getDbPort() + "/" +
                    MysqlConfig.getDbName() + "?" + MysqlConfig.getDbCondition();
            SpiderApplication.connection = DriverManager.getConnection(url, MysqlConfig.getDbUsername(), MysqlConfig
                    .getDbPassword());
            SpiderApplication.statement = SpiderApplication.connection.createStatement();
            LOGGER.info("database connect success");
            MysqlConfig.setConnectionSuccessful(true);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            MysqlConfig.setConnectionSuccessful(false);
            Alerts.showError(SpiderValueConsts.MAIN_TITLE, e.getMessage());
        }
    }
}
