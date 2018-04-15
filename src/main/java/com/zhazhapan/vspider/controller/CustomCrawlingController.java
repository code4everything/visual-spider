package com.zhazhapan.vspider.controller;

import com.zhazhapan.vspider.SpiderApplication;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * @author pantao
 * @since 2018/4/15
 */
public class CustomCrawlingController {

    @FXML
    public CheckBox enableCustomCrawling;

    @FXML
    public TextField dbHost;

    @FXML
    public TextField dbPort;

    @FXML
    public TextField dbName;

    @FXML
    public TextField dbCondition;

    @FXML
    public TextField dbUsername;

    @FXML
    public TextField dbPassword;

    @FXML
    public TextField dbTable;

    @FXML
    public TextArea mappings;

    @FXML
    public CheckBox enableSql;

    @FXML
    private void initialize() {
        SpiderApplication.customCrawlingController = this;
    }
}
