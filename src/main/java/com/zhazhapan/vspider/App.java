package com.zhazhapan.vspider;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

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

/**
 * @author pantao
 *
 */
public class App extends Application {

	private static Logger logger = Logger.getLogger(App.class);

	public static MainController mainController = null;

	public static VsController controller = new VsController();

	public static String[] domains;

	public static ArrayList<String> visitUrls = new ArrayList<String>();

	public static ArrayList<String> downloadUrls = new ArrayList<String>();

	public static int crawlingDelay = DefaultConfigValues.POLITENESS_DELAY;

	public static Pattern downloadFilterPattern = Pattern.compile(".*");

	public static Pattern crawlFilterPattern = Pattern.compile(".*");

	public static Pattern visitFilterPattern = Pattern.compile(".*");

	public static String DOWNLOAD_FOLDER = DefaultConfigValues.CRAWL_STORAGE_FOLDER + Values.SEPARATOR + "files"
			+ Values.SEPARATOR + Formatter.datetimeToCustomString(new Date(), "yyyyMMdd");

	public static void main(String[] args) {
		logger.info("start to run app");
		initThreadPool();
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		try {
			BorderPane root = (BorderPane) FXMLLoader.load(getClass().getResource("view/MainWindow.fxml"));
			stage.setScene(new Scene(root));
		} catch (Exception e) {
			logger.error("load fxml error: " + e.getMessage());
		}
		stage.setTitle(Values.MAIN_TITLE);
		stage.getIcons().add(new Image(getClass().getResourceAsStream("view/spider.jpg")));
		stage.show();
		stage.setOnCloseRequest((WindowEvent event) -> {
			stage.setIconified(true);
			event.consume();
		});
	}

	public static void initThreadPool() {
		ThreadPool.setCorePoolSize(1);
		ThreadPool.setMaximumPoolSize(5);
		ThreadPool.setKeepAliveTime(100);
		ThreadPool.setTimeUnit(TimeUnit.MILLISECONDS);
		ThreadPool.init();
	}
}
