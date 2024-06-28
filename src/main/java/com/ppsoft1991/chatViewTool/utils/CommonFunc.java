package com.ppsoft1991.chatViewTool.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class CommonFunc {

    public static Connection openDB(final String dbPath) {
        final String driver = "jdbc:sqlite:" + dbPath;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(driver);
            conn.setAutoCommit(false);
        } catch (Exception err) {
            System.out.println("[-] " + err.getMessage());
        }
        return conn;
    }

    public static void mergeDb(final List<String> dbFileList, final String dir) throws Exception {
        // 删除缓存文件
        CommonUtils.deleteFile(dir + "/MSG_ALL.db");
        CommonUtils.deleteFile(dir + "/MediaMSG_ALL.db");
        // 生成聊天数据库
        generateDb(dir, "MSG_ALL.db");
        generateMSG(dir, "MSG_ALL.db");
        // 合并 MSG 数据库
        MsgDB.init(dir+"/MSG_ALL.db");
        for (String dbFileName : dbFileList) {
            if (dbFileName.startsWith("MSG") && !dbFileName.startsWith("MSG_ALL")) {
                String dbFilePath = dir + "/" + dbFileName + "_dec.db";
                System.out.println("Merge " + dbFilePath + " to MSG_ALL.db");

                final String sql = "ATTACH DATABASE '" + dbFilePath + "' AS example; " +
                        "INSERT INTO Msg (TalkerId,Type,IsSender,CreateTime,Sequence,MsgSequence,StrTalker,StrContent,MsgSvrID,BytesExtra) " +
                        "SELECT TalkerId,Type,IsSender,CreateTime,Sequence,MsgSequence,StrTalker,StrContent,MsgSvrID,BytesExtra FROM example.Msg; " +
                        "DETACH DATABASE example;";
                try {
                    MsgDB.getMsgTemplate().update(sql);
                }catch (Exception err) {
                    System.out.println("[-] Merge MsgDB error: " + dbFilePath);
                    err.printStackTrace();
                }
//                try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dir + "/MSG_ALL.db");
//                     Statement stmt = conn.createStatement()) {
//                    conn.setAutoCommit(false); // 开启事务
//                    stmt.executeUpdate(sql);
//                    conn.commit(); // 提交事务
//                    System.out.println("[+] 合并 " + dbFilePath + " 完成");
//                } catch (Exception e) {
//                    System.out.println("[-] Merge MsgDB error: " + dbFilePath);
//                    e.printStackTrace();
//                }
            }
        }
        MsgDB.close();
        // 合并 MediaMSG 数据库
        generateDb(dir, "MediaMSG_ALL.db");
        generateMediaMsg(dir, "MediaMSG_ALL.db");
        MsgDB.init(dir+"/MediaMSG_ALL.db");
        for (String dbFileName : dbFileList) {
            if (dbFileName.startsWith("MediaMSG") && !dbFileName.startsWith("MediaMSG_ALL")) {
                String dbFilePath = dir + "/" + dbFileName + "_dec.db";
                System.out.println("Merge " + dbFilePath + " to MediaMSG_ALL.db");

                final String sql = "ATTACH DATABASE '" + dbFilePath + "' AS example; " +
                        "INSERT INTO Media (Key,Reserved0,Buf,Reserved1,Reserved2) " +
                        "SELECT Key,Reserved0,Buf,Reserved1,Reserved2 FROM example.Media; " +
                        "DETACH DATABASE example;";
                try {
                    MsgDB.getMsgTemplate().update(sql);
                }catch (Exception err) {
                    System.out.println("[-] Merge MediaMsg error: " + dbFilePath);
                    err.printStackTrace();
                }
//                try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dir + "/MediaMSG_ALL.db");
//                     Statement stmt = conn.createStatement()) {
//                    conn.setAutoCommit(false); // 开启事务
//                    stmt.executeUpdate(sql);
//                    conn.commit(); // 提交事务
//                    System.out.println("[+] 合并 " + dbFilePath + " 完成");
//                } catch (Exception e) {
//                    System.out.println("[-] 合并语音数据库失败: " + dbFilePath);
//                    e.printStackTrace();
//                }
            }
        }
    }


    public static void generateDb(final String dir, String dbName) throws Exception {

        CommonUtils.deleteFile(dir + "/" + dbName);
        CommonUtils.deleteFile(dir + "/" + dbName + "_dec.db");
            //MsgDB.init(dir + "/" + dbName);
            //MsgDB.getMsgTemplate().execute("CREATE TABLE IF NOT EXISTS random (Key TEXT);");
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dir + "/" + dbName);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS random (Key TEXT);");
        } catch (Exception e) {
            System.out.println("[-] 生成数据库失败");
            e.printStackTrace();
        }
    }

    public static void generateMSG(final String dir, String msgName) throws Exception {
        final String generateMsgSql = "create table MSG\n" +
                "(\n" +
                "    localId         INTEGER\n" +
                "        primary key autoincrement,\n" +
                "    TalkerId        INT default 0,\n" +
                "    MsgSvrID        INT,\n" +
                "    Type            INT,\n" +
                "    SubType         INT,\n" +
                "    IsSender        INT,\n" +
                "    CreateTime      INT,\n" +
                "    Sequence        INT default 0,\n" +
                "    StatusEx        INT default 0,\n" +
                "    FlagEx          INT,\n" +
                "    Status          INT,\n" +
                "    MsgServerSeq    INT,\n" +
                "    MsgSequence     INT,\n" +
                "    StrTalker       TEXT,\n" +
                "    StrContent      TEXT,\n" +
                "    DisplayContent  TEXT,\n" +
                "    Reserved0       INT default 0,\n" +
                "    Reserved1       INT default 0,\n" +
                "    Reserved2       INT default 0,\n" +
                "    Reserved3       INT default 0,\n" +
                "    Reserved4       TEXT,\n" +
                "    Reserved5       TEXT,\n" +
                "    Reserved6       TEXT,\n" +
                "    CompressContent BLOB,\n" +
                "    BytesExtra      BLOB,\n" +
                "    BytesTrans      BLOB\n" +
                ");";
        final String generateIndexSql = "CREATE INDEX IF NOT EXISTS search_chat on \"MSG\" (strTalker, CreateTime);";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dir + "/" + msgName);
             Statement stmt = conn.createStatement()){
            stmt.execute(generateMsgSql);
            stmt.execute(generateIndexSql);
            System.out.println("[+] 创建MSG数据库成功:");
            System.out.println("[+] 创建MSG索引成功:");
        }catch (Exception e){
            System.out.println("[-] 创建MSG数据库失败:");
            System.out.println("[-] 创建MSG索引失败:");
            e.printStackTrace();
        }
    }

    public static void generateMediaMsg(String dir, String msgName){
        final String generateMediaMsgSql = "create table Media\n" +
                "(\n" +
                "    Key       TEXT\n" +
                "        primary key,\n" +
                "    Reserved0 INT,\n" +
                "    Buf       BLOB,\n" +
                "    Reserved1 INT,\n" +
                "    Reserved2 TEXT\n" +
                ");";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dir + "/" + msgName);
             Statement stmt = conn.createStatement()){
            stmt.execute(generateMediaMsgSql);
            System.out.println("[+] 创建MediaMsg数据库成功:");
        }catch (Exception e){
            System.out.println("[-] 创建MediaMsg数据库失败:");
            e.printStackTrace();
        }
    }

    public static ResultSet executeSql(final String dbPath, final String sql) {
        ResultSet result = null;
        try (Connection conn = openDB(dbPath);
             Statement stmt = conn.createStatement()) {
            result = stmt.executeQuery(sql);
        } catch (Exception err) {
            System.out.println("[-] " + err.getMessage());
        }
        return result;
    }

    private static void enableWALMode(Connection conn) throws Exception {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA journal_mode=WAL;");
        }
    }
}
