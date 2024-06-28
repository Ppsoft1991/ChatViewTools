package com.ppsoft1991.chatViewTool.utils;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.ppsoft1991.chatViewTool.mapper.msg.MsgMapper;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.beans.PropertyVetoException;
import java.util.List;
import java.util.Map;

public class MsgDB {
    private static JdbcTemplate MSG_TEMPLATE = null;
    private static String MSG_DB_PATH = null;
    private static ComboPooledDataSource dataSource;

    public static void init(String path) throws Exception {
        MSG_DB_PATH = path;
        if (MSG_TEMPLATE != null){
            dataSource = new ComboPooledDataSource();
            dataSource.setDriverClass("org.sqlite.JDBC");
            dataSource.setJdbcUrl("jdbc:sqlite:"+MSG_DB_PATH);
            MSG_TEMPLATE.getDataSource().getConnection().close();
            MSG_TEMPLATE = new JdbcTemplate(dataSource);
        }
    }

    public static void close(){
        if (dataSource != null){
            dataSource.close();
        }
    }

    public static JdbcTemplate getMsgTemplate() throws PropertyVetoException {
        if (MSG_TEMPLATE == null){
            if (MSG_DB_PATH == null){
                System.out.println("[-] MSG_DB_PATH is null");
            }
            ComboPooledDataSource dataSource = new ComboPooledDataSource();
            dataSource.setDriverClass("org.sqlite.JDBC");
            dataSource.setJdbcUrl("jdbc:sqlite:"+MSG_DB_PATH);
            MSG_TEMPLATE = new JdbcTemplate(dataSource);
            //generateStrTalkerIndex();
        }
        return MSG_TEMPLATE;
    }


    public static List<MsgMapper> searchMessageFromText(final String text) throws PropertyVetoException {
        String sql = "select * FROM MSG INDEXED BY search_chat where StrContent like '%"+text+"%';";
        return getMsgTemplate().query(sql, new BeanPropertyRowMapper<>(MsgMapper.class));
    }

    public static List<Map<String, Object>> searchFilterMessageFromWxid(final String wxid, final String filterStr) throws PropertyVetoException {
        final String sql = "select id from" +
                "(select row_number() over () as id,* FROM MSG INDEXED BY search_chat where StrTalker='"+wxid+"' ORDER BY CreateTime)" +
                "where StrContent like '%"+filterStr+"%';";
        return getMsgTemplate().queryForList(sql);
    }

    public static Integer getMsgCount(String wxid) throws PropertyVetoException {
        final String sql = "SELECT count(*) AS num FROM MSG WHERE StrTalker='" + wxid + "'";
        return MsgDB.getMsgTemplate().queryForObject(sql, Integer.class);
    }

    private static void generateStrTalkerIndex(){
        final String sql = "CREATE INDEX IF NOT EXISTS search_chat on \"MSG\" (strTalker, CreateTime);";
        try {
            getMsgTemplate().execute(sql);
            System.out.println("[+] 创建索引成功:");
        }catch (Exception e){
            System.out.println("[-] 创建索引失败:");
            e.printStackTrace();
        }
    }

    public static List<MsgMapper> queryChatMsgLimit(String wxid, int currentPage) throws PropertyVetoException {
        final String sql = "SELECT * FROM MSG INDEXED BY search_chat where StrTalker='" + wxid + "' ORDER BY CreateTime LIMIT " + currentPage * 500 + ",500";
        return getMsgTemplate().query(sql, new BeanPropertyRowMapper<>(MsgMapper.class));
    }

    public static void generateMSG() throws PropertyVetoException {
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
        try {
            getMsgTemplate().execute(generateMsgSql);
            System.out.println("[+] 创建MSG数据库成功:");
        }catch (Exception e){
            System.out.println("[-] 创建MSG数据库失败:");
            e.printStackTrace();
        }

        try {
            getMsgTemplate().execute(generateIndexSql);
            System.out.println("[+] 创建MSG索引成功:");
        }catch (Exception e){
            System.out.println("[-] 创建MSG索引失败:");
            e.printStackTrace();
        }
    }



}
