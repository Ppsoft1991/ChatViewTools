package com.ppsoft1991.chatViewTool.mapper.microMsg;


public class ChatRoomMapper {

  private String chatRoomName;
  private String userNameList;
  private String displayNameList;
  private Integer chatRoomFlag;
  private Integer owner;
  private Integer isShowName;
  private String selfDisplayName;
  private Integer reserved1;
  private String reserved2;
  private Integer reserved3;
  private String reserved4;
  private Integer reserved5;
  private String reserved6;
  private String roomData;
  private Integer reserved7;
  private String reserved8;

  @Override
  public String toString() {
    return "ChatRoomMapper{" +
            "chatRoomName='" + chatRoomName + '\'' +
            ", userNameList='" + userNameList + '\'' +
            ", displayNameList='" + displayNameList + '\'' +
            ", chatRoomFlag=" + chatRoomFlag +
            ", owner=" + owner +
            ", isShowName=" + isShowName +
            ", selfDisplayName='" + selfDisplayName + '\'' +
            ", reserved1=" + reserved1 +
            ", reserved2='" + reserved2 + '\'' +
            ", reserved3=" + reserved3 +
            ", reserved4='" + reserved4 + '\'' +
            ", reserved5=" + reserved5 +
            ", reserved6='" + reserved6 + '\'' +
            ", roomData='" + roomData + '\'' +
            ", reserved7=" + reserved7 +
            ", reserved8='" + reserved8 + '\'' +
            '}';
  }

  public String getChatRoomName() {
    return chatRoomName;
  }

  public void setChatRoomName(String chatRoomName) {
    this.chatRoomName = chatRoomName;
  }


  public String getUserNameList() {
    return userNameList;
  }

  public void setUserNameList(String userNameList) {
    this.userNameList = userNameList;
  }


  public String getDisplayNameList() {
    return displayNameList;
  }

  public void setDisplayNameList(String displayNameList) {
    this.displayNameList = displayNameList;
  }


  public Integer getChatRoomFlag() {
    return chatRoomFlag;
  }

  public void setChatRoomFlag(Integer chatRoomFlag) {
    this.chatRoomFlag = chatRoomFlag;
  }


  public Integer getOwner() {
    return owner;
  }

  public void setOwner(Integer owner) {
    this.owner = owner;
  }


  public Integer getIsShowName() {
    return isShowName;
  }

  public void setIsShowName(Integer isShowName) {
    this.isShowName = isShowName;
  }


  public String getSelfDisplayName() {
    return selfDisplayName;
  }

  public void setSelfDisplayName(String selfDisplayName) {
    this.selfDisplayName = selfDisplayName;
  }


  public Integer getReserved1() {
    return reserved1;
  }

  public void setReserved1(Integer reserved1) {
    this.reserved1 = reserved1;
  }


  public String getReserved2() {
    return reserved2;
  }

  public void setReserved2(String reserved2) {
    this.reserved2 = reserved2;
  }


  public Integer getReserved3() {
    return reserved3;
  }

  public void setReserved3(Integer reserved3) {
    this.reserved3 = reserved3;
  }


  public String getReserved4() {
    return reserved4;
  }

  public void setReserved4(String reserved4) {
    this.reserved4 = reserved4;
  }


  public Integer getReserved5() {
    return reserved5;
  }

  public void setReserved5(Integer reserved5) {
    this.reserved5 = reserved5;
  }


  public String getReserved6() {
    return reserved6;
  }

  public void setReserved6(String reserved6) {
    this.reserved6 = reserved6;
  }


  public String getRoomData() {
    return roomData;
  }

  public void setRoomData(String roomData) {
    this.roomData = roomData;
  }


  public Integer getReserved7() {
    return reserved7;
  }

  public void setReserved7(Integer reserved7) {
    this.reserved7 = reserved7;
  }


  public String getReserved8() {
    return reserved8;
  }

  public void setReserved8(String reserved8) {
    this.reserved8 = reserved8;
  }


}
