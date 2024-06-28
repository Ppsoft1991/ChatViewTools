package com.ppsoft1991.chatViewTool.mapper.microMsg;

public class HeaderIconMapper {
    private String usrName;

    public String getBigHeadImgUrl() {
        return bigHeadImgUrl;
    }

    public void setBigHeadImgUrl(String bigHeadImgUrl) {
        this.bigHeadImgUrl = bigHeadImgUrl;
    }

    public String getUsrName() {
        return usrName;
    }

    public void setUsrName(String usrName) {
        this.usrName = usrName;
    }

    public String getSmallHeadImgUrl() {
        return smallHeadImgUrl;
    }

    public void setSmallHeadImgUrl(String smallHeadImgUrl) {
        this.smallHeadImgUrl = smallHeadImgUrl;
    }

    private String smallHeadImgUrl;
    private String bigHeadImgUrl;
}
