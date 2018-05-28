package com.zhazhapan.vspider.models;

import com.zhazhapan.util.Checker;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pantao
 * @since 2018/4/15
 */
public class MysqlConfig {

    private static boolean enableCustom = false;

    private static String dbHost = "127.0.0.1";

    private static String dbPort = "3306";

    private static String dbName = "spider";

    private static String dbCondition = "useUnicode=true&characterEncoding=utf-8&useSSL=true&serverTimezone=UTC";

    private static String dbUsername = "root";

    private static String dbPassword = "123456";

    private static String tableName = "table";

    private static List<Pair<String, String>> fields = new ArrayList<>();

    private static boolean enableSql = false;

    private static boolean connectionSuccessful = false;

    public static boolean isConnectionSuccessful() {
        return connectionSuccessful;
    }

    public static void setConnectionSuccessful(boolean connectionSuccessful) {
        MysqlConfig.connectionSuccessful = connectionSuccessful;
    }

    public static boolean isEnableSql() {
        return enableSql;
    }

    public static void setEnableSql(boolean enableSql) {
        MysqlConfig.enableSql = enableSql;
    }

    public static String getTableName() {
        return tableName;
    }

    public static void setTableName(String tableName) {
        if (Checker.isNotEmpty(tableName)) {
            MysqlConfig.tableName = tableName;
        }
    }

    public static List<Pair<String, String>> getFields() {
        return fields;
    }

    public static boolean isEnableCustom() {
        return enableCustom;
    }

    public static void setEnableCustom(boolean enableCustom) {
        MysqlConfig.enableCustom = enableCustom;
    }

    public static String getDbHost() {
        return dbHost;
    }

    public static void setDbHost(String dbHost) {
        if (Checker.isNotEmpty(dbHost)) {
            MysqlConfig.dbHost = dbHost;
        }
    }

    public static String getDbPort() {
        return dbPort;
    }

    public static void setDbPort(String dbPort) {
        if (Checker.isNotEmpty(dbPort)) {
            MysqlConfig.dbPort = dbPort;
        }
    }

    public static String getDbName() {
        return dbName;
    }

    public static void setDbName(String dbName) {
        if (Checker.isNotEmpty(dbName)) {
            MysqlConfig.dbName = dbName;
        }
    }

    public static String getDbCondition() {
        return dbCondition;
    }

    public static void setDbCondition(String dbCondition) {
        if (Checker.isNotEmpty(dbCondition)) {
            MysqlConfig.dbCondition = dbCondition;
        }
    }

    public static String getDbUsername() {
        return dbUsername;
    }

    public static void setDbUsername(String dbUsername) {
        if (Checker.isNotEmpty(dbUsername)) {
            MysqlConfig.dbUsername = dbUsername;
        }
    }

    public static String getDbPassword() {
        return dbPassword;
    }

    public static void setDbPassword(String dbPassword) {
        if (Checker.isNotEmpty(dbPassword)) {
            MysqlConfig.dbPassword = dbPassword;
        }
    }
}
