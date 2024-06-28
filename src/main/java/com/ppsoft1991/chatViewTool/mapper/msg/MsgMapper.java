package com.ppsoft1991.chatViewTool.mapper.msg;


public class MsgMapper {

  private Integer Id;
  private Integer localId;
  private Integer talkerId;
  private Long msgSvrId;
  private Integer type;
  private Integer subType;
  private Integer isSender;
  private Integer createTime;
  private Integer sequence;
  private Integer statusEx;
  private Integer flagEx;
  private Integer status;
  private Integer msgServerSeq;
  private Integer msgSequence;
  private String strTalker;
  private String strContent;
  private String displayContent;
  private Integer reserved0;
  private Integer reserved1;
  private Integer reserved2;
  private Integer reserved3;
  private String reserved4;
  private String reserved5;
  private String reserved6;
  private String compressContent;
  private byte[] bytesExtra;
  private String bytesTrans;

  @Override
  public String toString() {
    return "Msg{" +
            "id=" + Id +
            "localId=" + localId +
            ", talkerId=" + talkerId +
            ", msgSvrId=" + msgSvrId +
            ", type=" + type +
            ", subType=" + subType +
            ", isSender=" + isSender +
            ", createTime=" + createTime +
            ", sequence=" + sequence +
            ", statusEx=" + statusEx +
            ", flagEx=" + flagEx +
            ", status=" + status +
            ", msgServerSeq=" + msgServerSeq +
            ", msgSequence=" + msgSequence +
            ", strTalker='" + strTalker + '\'' +
            ", strContent='" + strContent + '\'' +
            ", displayContent='" + displayContent + '\'' +
            ", reserved0=" + reserved0 +
            ", reserved1=" + reserved1 +
            ", reserved2=" + reserved2 +
            ", reserved3=" + reserved3 +
            ", reserved4='" + reserved4 + '\'' +
            ", reserved5='" + reserved5 + '\'' +
            ", reserved6='" + reserved6 + '\'' +
            ", compressContent='" + compressContent + '\'' +
            ", bytesExtra='" + bytesExtra + '\'' +
            ", bytesTrans='" + bytesTrans + '\'' +
            '}';
  }

  public Integer getLocalId() {
    return localId;
  }

  public void setLocalId(Integer localId) {
    this.localId = localId;
  }


  public Integer getTalkerId() {
    return talkerId;
  }

  public void setTalkerId(Integer talkerId) {
    this.talkerId = talkerId;
  }


  public Long getMsgSvrId() {
    return msgSvrId;
  }

  public void setMsgSvrId(Long msgSvrId) {
    this.msgSvrId = msgSvrId;
  }


  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }


  public Integer getSubType() {
    return subType;
  }

  public void setSubType(Integer subType) {
    this.subType = subType;
  }


  public Integer getIsSender() {
    return isSender;
  }

  public void setIsSender(Integer isSender) {
    this.isSender = isSender;
  }


  public Integer getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Integer createTime) {
    this.createTime = createTime;
  }


  public Integer getSequence() {
    return sequence;
  }

  public void setSequence(Integer sequence) {
    this.sequence = sequence;
  }


  public Integer getStatusEx() {
    return statusEx;
  }

  public void setStatusEx(Integer statusEx) {
    this.statusEx = statusEx;
  }


  public Integer getFlagEx() {
    return flagEx;
  }

  public void setFlagEx(Integer flagEx) {
    this.flagEx = flagEx;
  }


  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }


  public Integer getMsgServerSeq() {
    return msgServerSeq;
  }

  public void setMsgServerSeq(Integer msgServerSeq) {
    this.msgServerSeq = msgServerSeq;
  }


  public Integer getMsgSequence() {
    return msgSequence;
  }

  public void setMsgSequence(Integer msgSequence) {
    this.msgSequence = msgSequence;
  }


  public String getStrTalker() {
    return strTalker;
  }

  public void setStrTalker(String strTalker) {
    this.strTalker = strTalker;
  }


  public String getStrContent() {
    return strContent;
  }

  public void setStrContent(String strContent) {
    this.strContent = strContent;
  }


  public String getDisplayContent() {
    return displayContent;
  }

  public void setDisplayContent(String displayContent) {
    this.displayContent = displayContent;
  }


  public Integer getReserved0() {
    return reserved0;
  }

  public void setReserved0(Integer reserved0) {
    this.reserved0 = reserved0;
  }


  public Integer getReserved1() {
    return reserved1;
  }

  public void setReserved1(Integer reserved1) {
    this.reserved1 = reserved1;
  }


  public Integer getReserved2() {
    return reserved2;
  }

  public void setReserved2(Integer reserved2) {
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


  public String getReserved5() {
    return reserved5;
  }

  public void setReserved5(String reserved5) {
    this.reserved5 = reserved5;
  }


  public String getReserved6() {
    return reserved6;
  }

  public void setReserved6(String reserved6) {
    this.reserved6 = reserved6;
  }


  public String getCompressContent() {
    return compressContent;
  }

  public void setCompressContent(String compressContent) {
    this.compressContent = compressContent;
  }


  public byte[] getBytesExtra() {
    return bytesExtra;
  }

  public void setBytesExtra(byte[] bytesExtra) {
    this.bytesExtra = bytesExtra;
  }


  public String getBytesTrans() {
    return bytesTrans;
  }

  public void setBytesTrans(String bytesTrans) {
    this.bytesTrans = bytesTrans;
  }

  public Integer getId() {
    return Id;
  }

  public void setId(Integer id) {
    Id = id;
  }
}
