package com.ppsoft1991.chatViewTool.controller;

import com.ppsoft1991.chatViewTool.fx.BubbleBox;
import com.ppsoft1991.chatViewTool.fx.MessageBox;
import com.ppsoft1991.chatViewTool.fx.NoticeBox;
import com.ppsoft1991.chatViewTool.mapper.microMsg.ChatRoomMapper;
import com.ppsoft1991.chatViewTool.mapper.msg.MsgMapper;
import com.ppsoft1991.chatViewTool.utils.CommonUtils;
import com.ppsoft1991.chatViewTool.utils.MicroMsgDB;
import com.ppsoft1991.chatViewTool.utils.MsgDB;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.beans.PropertyVetoException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;


public class ChatController {

    public Label searchNumber;
    private ChatAppController chatApp;
    public Button searchButton;
    public Button jumpButton;
    public Button jumoSearchButton;
    @FXML
    private VBox listMsg;
    @FXML
    public Pane toolPane;
    @FXML
    private Label labelPage;
    @FXML
    private Button lastButton;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Button nextButton;
    private boolean showHeader;
    public static String absolutePath;
    private int currentPage;
    private int msgCount;
    private String wxid;
    @FXML
    public TextField FieldText;
    public String filterStr;

    private ArrayList<Integer> chatLocalId;
    private ArrayList<Integer> chatLocalIdPage;
    private int jumpPoint=-1;


    final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private HashMap<String, String> GROUP_USER_HEADER = new HashMap<>();
    private HashMap<String, String> GROUP_USER_NICKNAME = new HashMap<>();

    private List<String> wechatId;
    private List<String> nickName;


    public ChatController() {
        this.currentPage = 0;
        this.msgCount = 0;
        this.filterStr = null;
    }

    public static ImageView setImage(final String uri) {
        ImageView imgHead = new ImageView(uri);
        imgHead.setFitHeight(60.0);
        imgHead.setFitWidth(60.0);
        return imgHead;
    }

    public void getBasicInfo() throws PropertyVetoException {
        ChatRoomMapper chatRoomMapper = MicroMsgDB.queryChatRoom(wxid);
        wechatId =  Arrays.asList(chatRoomMapper.getUserNameList().split("\\^G"));
        nickName =  Arrays.asList(chatRoomMapper.getDisplayNameList().split("\\^G"));
        System.out.println(String.join(",",wechatId));
        System.out.println(String.join(",",nickName));
        System.out.println(wechatId.size());
        System.out.println(nickName.size());
        for (String wx:wechatId){
            CommonUtils.put(wx, CommonUtils.WXHEADER_MAP, GROUP_USER_HEADER);
            CommonUtils.put(wx, CommonUtils.WXID_MAP, GROUP_USER_NICKNAME);
        }
    }

    public void getGroupUser(){
        // 聊天室，多搜索一个用户名显示
        if (wxid.contains("@chatroom")){
            try {

                if (wechatId.size()==nickName.size()) {
                    ArrayList<String> list = new ArrayList<>();
                    for (int i = 0; i < wechatId.size(); i++) {
                        String id = wechatId.get(i);
                        String nick = nickName.get(i);
                        list.add(id+" (" + nickName.get(i) + ")");
                        if (!GROUP_USER_NICKNAME.containsKey(id)){
                            GROUP_USER_NICKNAME.put(id, nick);
                        }
                    }
                    String result = String.join(",", list);
                    listMsg.getChildren().add(new NoticeBox(result));
                }else {
                    String result = String.join(",", wechatId);
                    listMsg.getChildren().add(new NoticeBox(result));
                }
            }catch (Exception ignored){
                listMsg.getChildren().add(new NoticeBox("未读到群成员或者群成员数据库MicroMsg解析失败!"));
            }
        }
    }

    @FXML
    public void InsertContent(final String strContent,String wxid, final String createDate, final String selfSend, String image) {
        showChatContent(createDate,wxid, strContent, "1".equals(selfSend), setImage(image));
    }

    public void setInitValue(final String wxid, final boolean loadHeadImg, final int msgCount, final String path) throws SQLException, PropertyVetoException {
        this.showHeader = loadHeadImg;
        absolutePath = path;
        this.msgCount = msgCount / 500 + 1;
        this.wxid = wxid;
        GROUP_USER_HEADER = new HashMap<>();
        GROUP_USER_NICKNAME = new HashMap<>();
        if (wxid.contains("@chatroom")){
            getBasicInfo();
        }
        this.getChatInfo();
    }

    public String getUserHeader(String wxid){
        if (GROUP_USER_HEADER.containsKey(wxid)){
            return GROUP_USER_HEADER.get(wxid);
        }else {
            return CommonUtils.BLACK_Header;
        }
    }

    public String getNickName(String wxid){
        if (GROUP_USER_NICKNAME.containsKey(wxid)){
            return GROUP_USER_NICKNAME.get(wxid)+"("+wxid+")";
        }
        return wxid;
    }

    public void getChatInfo() throws SQLException, PropertyVetoException {
        if (currentPage!=jumpPoint) {
            this.listMsg.getChildren().clear();
            List<MsgMapper> msgMappers = MsgDB.queryChatMsgLimit(wxid, currentPage);
            String headPath = CommonUtils.BLACK_Header;
            final int count = this.currentPage + 1;
            if (this.msgCount > 1) {
                this.lastButton.setDisable(false);
                this.nextButton.setDisable(false);
                this.labelPage.setText("page " + count + " of " + this.msgCount);
            }
            if (this.showHeader) {
                headPath = CommonUtils.WXHEADER_MAP.get(wxid);
            }
            if (count == 1) {
                getGroupUser();
            }
            if (!msgMappers.isEmpty()) {
                for (MsgMapper msgMapper : msgMappers) {
                    String res = CommonUtils.parseChatStr(String.valueOf(msgMapper.getType()), msgMapper);
                    if (res.startsWith("[系统消息]")) {
                        listMsg.getChildren().add(new NoticeBox(res));
                    } else {
                        String tmpName = "";
                        if (wxid.contains("@chatroom") && 1 != (msgMapper.getIsSender())) {
                            try {
                                tmpName = CommonUtils.getWxid(msgMapper.getBytesExtra());
                            } catch (Exception exception) {
                                System.out.println("[-] Get group username error!!");
                            }
                            headPath = getUserHeader(tmpName);
                            tmpName = getNickName(tmpName);
                        }
                        InsertContent(res, tmpName, msgMapper.getCreateTime() + "", String.valueOf(msgMapper.getIsSender()), headPath);
                    }
                }
            }
        }
        // 说明有搜索到聊天内容
        if (chatLocalIdPage!=null && chatLocalIdPage.contains(currentPage)){
            cacheBubblePod();
        }
    }

    public static String downloadImg(String url, final String fileName) {
        url = url.replace("http:", "https:");
        if (!url.isEmpty()) {
            System.out.println("Downloading:" + url);
            try {
                final URL httpUrl = new URL(url);
                final URLConnection conn = httpUrl.openConnection();
                final InputStream inputStream = conn.getInputStream();
                final FileOutputStream fos = new FileOutputStream(fileName);
                int i;
                while ((i = inputStream.read()) != -1) {
                    fos.write(i);
                }
                inputStream.close();
            } catch (Exception e) {
                System.out.println("[-] download url error: " + url + e.getMessage());
            }
            return CommonUtils.BLACK_Header;
        }return null;
    }



    @FXML
    public void onSearchButtonClick() throws PropertyVetoException, SQLException {
        this.filterStr = this.FieldText.getText();
        if (this.filterStr.isEmpty()) {
            return;
        }
        jumpPoint = 0;
        chatLocalId = new ArrayList<>();
        List<Map<String, Object>> ids = MsgDB.searchFilterMessageFromWxid(wxid, filterStr);
        if (!ids.isEmpty()){
            for (Map<String, Object> next : ids) {
                chatLocalId.add(Integer.parseInt(String.valueOf(next.get("id"))));
            }
            System.out.println("[+] find "+chatLocalId.size()+" chat record in total");
            chatLocalIdPage = new ArrayList<>();
            for (int i:chatLocalId){
                chatLocalIdPage.add(getChatPage(i));
            }
            System.out.println("in the page : "+chatLocalIdPage);
        }
        onNextButtonClicked();
    }

    public int getChatPage(int id){
        return (id / 500)+1;
    }

    private void setChatHighlight(int number){
        Node node = listMsg.getChildren().get(number-1);
        listMsg.getChildren().get(number-1).setStyle("-fx-background-color: #c0c0c0");
        if (node instanceof HBox) {
            for (final Node nodeIn : ((HBox)node).getChildren()) {
                if (nodeIn instanceof Label) {
                    final Label tempLabel = (Label)nodeIn;
                    System.out.println("匹配到:" + tempLabel.getText());
                }
            }
        }
    }

    private void cacheBubblePod() {
        // 获取相对页数
        ArrayList<Integer> pageList = new ArrayList<>();
        int page=0;
        for (int i1=0;i1<chatLocalIdPage.size();i1++){
            if (chatLocalIdPage.get(i1)==currentPage+1){
                page = chatLocalId.get(i1);
                if (page>500){
                    page=page%500;
                }
                pageList.add(page);
            }
        }

        // 给相对页数着色
        for (int i2=0;i2<pageList.size();i2++){
            setChatHighlight(pageList.get(i2));
            if (i2==0){
                setChatScrollPane(pageList.get(i2));
            }
        }
    }

    public void setChatScrollPane(int number) {
        ObservableList<Node> children = listMsg.getChildren();
        if (number < 0 || number >= children.size()) {
            return; // 索引越界时直接返回
        }
        listMsg.applyCss();
        listMsg.layout();
        Node targetNode = children.get(number);
        double targetY = 0;
        for (int i = 0; i < number; i++) {
            targetY += children.get(i).getBoundsInParent().getHeight() + listMsg.getSpacing();
        }
        double nodeHeight = targetNode.getBoundsInParent().getHeight();

        double scrollHeight = scrollPane.getContent().getBoundsInParent().getHeight();
        double viewportHeight = scrollPane.getViewportBounds().getHeight();

        // 置中目标节点
        double newVvalue = (targetY + 0.5 * nodeHeight - 0.5 * viewportHeight) / (scrollHeight - viewportHeight);
        newVvalue = Math.max(0, Math.min(newVvalue, 1)); // 保证滚动值在合法范围内

        double finalNewVvalue = newVvalue;
        scrollPane.setVvalue(finalNewVvalue);
        searchNumber.setText("[page of "+jumpPoint+ ","+number+"]");

    }


    @FXML
    private void onLastButtonClicked() throws SQLException, PropertyVetoException {
        if (currentPage>=0){
            this.listMsg.getChildren().clear();
            currentPage = currentPage-1;
            getChatInfo();
            this.scrollPane.setVvalue(0.0);
        }
        System.gc();
    }

    @FXML
    private void onNextButtonClicked() throws SQLException, PropertyVetoException {
        if (currentPage<=msgCount){
            currentPage = currentPage+1;
            getChatInfo();
            this.scrollPane.setVvalue(0.0);
        }
        System.gc();
    }

    public void showChatContent(String createDate,String wxid, String strContent, boolean selfSend, ImageView imgHead){
        // final String message = wxid+" [" + format.format(new Date(Integer.parseInt(createDate) * 1000L)) + "]\r\n"+strContent;
        //MessageBox messageBox = new MessageBox(message, selfSend, imgHead);
        //messageBox.prefWidthProperty().bind(scrollPane.widthProperty().subtract(20));
        final String message = wxid+" [" + format.format(new Date(Integer.parseInt(createDate) * 1000L)) + "]";
        BubbleBox bubbleBox = new BubbleBox(strContent, message, selfSend, imgHead);
        bubbleBox.prefWidthProperty().bind(scrollPane.widthProperty().subtract(20));
        listMsg.getChildren().add(bubbleBox);
    }

    public void onJumpButtonClick(ActionEvent actionEvent) throws SQLException, PropertyVetoException {
        int i = Integer.parseInt(FieldText.getText());
        if (i>=0 && i<=msgCount){
            this.listMsg.getChildren().clear();
            currentPage = i-1;
            this.scrollPane.setVvalue(0.0);
            getChatInfo();
        }
        System.gc();
    }

    public void jumpToPage(int pageNumber) throws SQLException, PropertyVetoException {
        if (pageNumber>=0 && pageNumber<=msgCount){
            this.listMsg.getChildren().clear();
            currentPage = pageNumber-1;
            this.scrollPane.setVvalue(0.0);
            getChatInfo();
        }
        System.gc();
    }

    public void onNextSearchButtonClick(ActionEvent actionEvent) throws PropertyVetoException, SQLException {
        if (chatLocalIdPage == null) {
            onSearchButtonClick();
        } else {
            if (jumpPoint >= chatLocalIdPage.size()) {
                jumpPoint = 0;
            }
            Integer jumpTo = chatLocalIdPage.get(jumpPoint);
            jumpToPage(jumpTo);
            int page = chatLocalId.get(jumpPoint);
            if (page > 500) {
                page = page % 500;
            }
            setChatHighlight(page);
            //setChatScrollPane(chatLocalId.get(jumpPoint));
            setChatScrollPane(page-1);
            System.out.println("[+] jump to " + jumpTo + " in the page " + page);
            jumpPoint = jumpPoint + 1;
        }
    }
}
