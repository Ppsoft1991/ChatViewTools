package com.ppsoft1991.chatViewTool.mapper.microMsg;


public class SessionMapper {

  private String strUsrName;
  private Integer nOrder;
  private Integer nUnReadCount;
  private String parentRef;
  private Integer reserved0;
  private String reserved1;
  private String strNickName;
  private Integer nStatus;
  private Integer nIsSend;
  private String strContent;
  private Integer nMsgType;
  private Integer nMsgLocalId;
  private Integer nMsgStatus;
  private Integer nTime;
  private String editContent;
  private Integer othersAtMe;
  private Integer reserved2;
  private String reserved3;
  private Integer reserved4;
  private String reserved5;
  private String bytesXml;

  @Override
  public String toString() {
    return "SessionMapper{" +
            "strUsrName='" + strUsrName + '\'' +
            ", nOrder=" + nOrder +
            ", nUnReadCount=" + nUnReadCount +
            ", parentRef='" + parentRef + '\'' +
            ", reserved0=" + reserved0 +
            ", reserved1='" + reserved1 + '\'' +
            ", strNickName='" + strNickName + '\'' +
            ", nStatus=" + nStatus +
            ", nIsSend=" + nIsSend +
            ", strContent='" + strContent + '\'' +
            ", nMsgType=" + nMsgType +
            ", nMsgLocalId=" + nMsgLocalId +
            ", nMsgStatus=" + nMsgStatus +
            ", nTime=" + nTime +
            ", editContent='" + editContent + '\'' +
            ", othersAtMe=" + othersAtMe +
            ", reserved2=" + reserved2 +
            ", reserved3='" + reserved3 + '\'' +
            ", reserved4=" + reserved4 +
            ", reserved5='" + reserved5 + '\'' +
            ", bytesXml='" + bytesXml + '\'' +
            '}';
  }

  public String getStrUsrName() {
    return strUsrName;
  }

  public void setStrUsrName(String strUsrName) {
    this.strUsrName = strUsrName;
  }


  public Integer getNOrder() {
    return nOrder;
  }

  public void setNOrder(Integer nOrder) {
    this.nOrder = nOrder;
  }


  public Integer getNUnReadCount() {
    return nUnReadCount;
  }

  public void setNUnReadCount(Integer nUnReadCount) {
    this.nUnReadCount = nUnReadCount;
  }


  public String getParentRef() {
    return parentRef;
  }

  public void setParentRef(String parentRef) {
    this.parentRef = parentRef;
  }


  public Integer getReserved0() {
    return reserved0;
  }

  public void setReserved0(Integer reserved0) {
    this.reserved0 = reserved0;
  }


  public String getReserved1() {
    return reserved1;
  }

  public void setReserved1(String reserved1) {
    this.reserved1 = reserved1;
  }


  public String getStrNickName() {
    return strNickName;
  }

  public void setStrNickName(String strNickName) {
    this.strNickName = strNickName;
  }


  public Integer getNStatus() {
    return nStatus;
  }

  public void setNStatus(Integer nStatus) {
    this.nStatus = nStatus;
  }


  public Integer getNIsSend() {
    return nIsSend;
  }

  public void setNIsSend(Integer nIsSend) {
    this.nIsSend = nIsSend;
  }


  public String getStrContent() {
    return strContent;
  }

  public void setStrContent(String strContent) {
    this.strContent = strContent;
  }


  public Integer getNMsgType() {
    return nMsgType;
  }

  public void setNMsgType(Integer nMsgType) {
    this.nMsgType = nMsgType;
  }


  public Integer getNMsgLocalId() {
    return nMsgLocalId;
  }

  public void setNMsgLocalId(Integer nMsgLocalId) {
    this.nMsgLocalId = nMsgLocalId;
  }


  public Integer getNMsgStatus() {
    return nMsgStatus;
  }

  public void setNMsgStatus(Integer nMsgStatus) {
    this.nMsgStatus = nMsgStatus;
  }


  public Integer getNTime() {
    return nTime;
  }

  public void setNTime(Integer nTime) {
    this.nTime = nTime;
  }


  public String getEditContent() {
    return editContent;
  }

  public void setEditContent(String editContent) {
    this.editContent = editContent;
  }


  public Integer getOthersAtMe() {
    return othersAtMe;
  }

  public void setOthersAtMe(Integer othersAtMe) {
    this.othersAtMe = othersAtMe;
  }


  public Integer getReserved2() {
    return reserved2;
  }

  public void setReserved2(Integer reserved2) {
    this.reserved2 = reserved2;
  }


  public String getReserved3() {
    return reserved3;
  }

  public void setReserved3(String reserved3) {
    this.reserved3 = reserved3;
  }


  public Integer getReserved4() {
    return reserved4;
  }

  public void setReserved4(Integer reserved4) {
    this.reserved4 = reserved4;
  }


  public String getReserved5() {
    return reserved5;
  }

  public void setReserved5(String reserved5) {
    this.reserved5 = reserved5;
  }


  public String getBytesXml() {
    return bytesXml;
  }

  public void setBytesXml(String bytesXml) {
    this.bytesXml = bytesXml;
  }

}
