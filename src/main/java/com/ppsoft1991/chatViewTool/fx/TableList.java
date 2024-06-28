package com.ppsoft1991.chatViewTool.fx;


public class TableList
{
    private String image;
    private String wechatId;
    private String userName;
    private String nickName;
    private String alias;
    private String strContent;
    private String filterCol;

    public TableList(final String header, final String wechatId, final String userName, final String nickName, final String alias, final String strContent, final String filterCol) {
        this.wechatId = wechatId;
        this.image = header;
        this.userName = userName;
        this.nickName = nickName;
        this.alias = alias;
        this.strContent = strContent;
        this.filterCol = filterCol;
    }

    public String getWechatId() {
        return this.wechatId;
    }

    public void setImage(String image){
        this.image = image;
    }

    public String getImage(){
        return this.image;
    }
    public String getUserName() {
        return this.userName;
    }

    public String getNickName() {
        return this.nickName;
    }

    public String getAlias() {
        return this.alias;
    }

    public String getStrContent() {
        return this.strContent;
    }

    public String getFilterCol() {
        return this.filterCol;
    }
}
