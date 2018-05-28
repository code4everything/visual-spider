package com.zhazhapan.vspider.controller;

import com.zhazhapan.util.Checker;
import com.zhazhapan.vspider.SpiderApplication;
import com.zhazhapan.vspider.models.MysqlConfig;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.Pair;

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
        // 初始化自定义爬取，并设置默认值
        SpiderApplication.customCrawlingController = this;
        mappings.setWrapText(true);
        dbHost.setText(MysqlConfig.getDbHost());
        dbPort.setText(MysqlConfig.getDbPort());
        dbCondition.setText(MysqlConfig.getDbCondition());
        dbName.setText(MysqlConfig.getDbName());
        dbPassword.setText(MysqlConfig.getDbPassword());
        dbTable.setText(MysqlConfig.getTableName());
        dbUsername.setText(MysqlConfig.getDbUsername());
        if (Checker.isNotEmpty(MysqlConfig.getFields())) {
            StringBuilder sb = new StringBuilder();
            for (Pair<String, String> pair : MysqlConfig.getFields()) {
                sb.append(pair.getKey()).append(" -> ").append(pair.getValue()).append(", ");
            }
            String mapping = sb.toString();
            mappings.setText(mapping.substring(0, mapping.length() - 2));
        }
    }
}
