package com.ppsoft1991.chatViewTool.utils;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.ppsoft1991.chatViewTool.mapper.microMsg.*;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.beans.PropertyVetoException;
import java.util.List;


public class MicroMsgDB {
    private static JdbcTemplate MICRO_MSG_TEMPLATE = null;
    private static String MICRO_MSG_DB_PATH = null;
    public static JdbcTemplate getMicroMsgTemplate() throws PropertyVetoException {
        if (MICRO_MSG_TEMPLATE==null) {
            ComboPooledDataSource dataSource = new ComboPooledDataSource();
            dataSource.setDriverClass("org.sqlite.JDBC");
            dataSource.setJdbcUrl("jdbc:sqlite:"+MICRO_MSG_DB_PATH);
            MICRO_MSG_TEMPLATE = new JdbcTemplate(dataSource);
        }
        return MICRO_MSG_TEMPLATE;
    }

    public static void init(String db_path) throws PropertyVetoException {
        MICRO_MSG_DB_PATH = db_path;
        if (MICRO_MSG_TEMPLATE != null){
            ComboPooledDataSource dataSource = new ComboPooledDataSource();
            dataSource.setDriverClass("org.sqlite.JDBC");
            dataSource.setJdbcUrl("jdbc:sqlite:"+MICRO_MSG_DB_PATH);
            MICRO_MSG_TEMPLATE = new JdbcTemplate(dataSource);
        }
    }

    public static HeaderIconMapper getUserHeadUrl(final String wxid) throws PropertyVetoException {
        final String sql = "SELECT usrName,smallHeadImgUrl,bigHeadImgUrl FROM ContactHeadImgUrl WHERE usrName ='" + wxid + "'";
        return getMicroMsgTemplate().query(sql, new BeanPropertyRowMapper<>(HeaderIconMapper.class)).get(0);
    }

    public static void getAllUserHeadUrl(String absolutePath) throws PropertyVetoException {
        final String sql = "SELECT usrName,smallHeadImgUrl,bigHeadImgUrl FROM ContactHeadImgUrl";
        for (HeaderIconMapper userHeader :getMicroMsgTemplate().query(sql, new BeanPropertyRowMapper<>(HeaderIconMapper.class))){
            CommonUtils.WXHEADER_MAP.put(userHeader.getUsrName(), getHeaderUrl(absolutePath,userHeader));
        }
    }

    public static String getSelfUserName() throws PropertyVetoException {
        final String sql="SELECT UserName FROM PatInfo";
        return getMicroMsgTemplate().query(sql, new BeanPropertyRowMapper<>(SelfUserMap.class)).get(0).getUsername();
    }

    public static String getHeaderUrl(String absolutePath,HeaderIconMapper userHeadUrl){
        return "file:" + absolutePath + "/.imgcache/" + userHeadUrl.getUsrName() + ".jpg";
    }

    public static List<SessionMapper> queryStrUsrName() throws PropertyVetoException {
        final String sql = "SELECT strUsrName from Session";
        return getMicroMsgTemplate().query(sql, new BeanPropertyRowMapper<>(SessionMapper.class));
    }

    public static List<SessionMapper> queryALLSession() throws PropertyVetoException {
        // 以前这里有个bug，order by 会报错
        final String sql = "SELECT * from Session  ORDER BY nOrder DESC"; // ORDER BY nOrder DESC";
        return getMicroMsgTemplate().query(sql, new BeanPropertyRowMapper<>(SessionMapper.class));
    }

    public static ContactMapper queryUserContact(String wxid) throws PropertyVetoException {
        final String sql = "SELECT UserName,Alias,NickName,Remark FROM Contact WHERE UserName = '" + wxid + "'";
        List<ContactMapper> mappers = getMicroMsgTemplate().query(sql, new BeanPropertyRowMapper<>(ContactMapper.class));
        if (mappers.size()>0){
            return getMicroMsgTemplate().query(sql, new BeanPropertyRowMapper<>(ContactMapper.class)).get(0);
        }else {
            return null;
        }

    }

    public static ChatRoomMapper queryChatRoom(String chatroom) throws PropertyVetoException {
        final String sql = "SELECT UserNameList,DisplayNameList FROM ChatRoom where ChatRoomName='" + chatroom + "'";
        List<ChatRoomMapper> mappers = getMicroMsgTemplate().query(sql, new BeanPropertyRowMapper<>(ChatRoomMapper.class));
        if (mappers.size()>0){
            return getMicroMsgTemplate().query(sql, new BeanPropertyRowMapper<>(ChatRoomMapper.class)).get(0);
        }else {
            return null;
        }
    }
}
