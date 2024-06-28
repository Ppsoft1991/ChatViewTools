package com.ppsoft1991.chatViewTool.controller;

import com.ppsoft1991.chatViewTool.mapper.microMsg.HeaderIconMapper;
import com.ppsoft1991.chatViewTool.utils.*;
import com.ppsoft1991.chatViewTool.fx.TableList;
import com.ppsoft1991.chatViewTool.mapper.microMsg.ContactMapper;
import com.ppsoft1991.chatViewTool.mapper.microMsg.SessionMapper;
import com.ppsoft1991.chatViewTool.mapper.msg.MsgMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.beans.PropertyVetoException;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.*;

public class ChatAppController implements Initializable{
    public TableColumn<Object, Object> image;
    @FXML
    private TableView<TableList> tableVw;
    @FXML
    private Label labelMsg;
    @FXML
    private TableColumn<TableList, String> wechatId;
    @FXML
    private TableColumn<TableList, String> userName;
    @FXML
    private TableColumn<TableList, String> nickName;
    @FXML
    private TableColumn<TableList, String> nameAlias;
    @FXML
    private TextField filterText;
    @FXML
    private TableColumn<TableList, String> strContent;
    private String absolutePath;
    @FXML
    private CheckBox loadHd;

    public ChatAppController() {
        this.absolutePath = null;
    }

    public void initTableView() {
        this.image.setCellValueFactory(new PropertyValueFactory<>("image"));
        this.wechatId.setCellValueFactory(new PropertyValueFactory<>("wechatId"));
        this.userName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        this.nickName.setCellValueFactory(new PropertyValueFactory<>("nickName"));
        this.nameAlias.setCellValueFactory(new PropertyValueFactory<>("alias"));
        this.strContent.setCellValueFactory(new PropertyValueFactory<>("strContent"));
    }

    public void pushToView(final String header, final String strUserName, final String userName, final String strNickName, final String alias, final String strContent, final String contentFilter) {
        final TableList tabList = new TableList(header, strUserName, userName, strNickName, alias, strContent, contentFilter);
        this.tableVw.getItems().add(tabList);
    }

    @FXML
    public void showChatWindow(final MouseEvent event) throws Exception {
        if (event.getClickCount() == 2) {
            this.labelMsg.setText("");
            MsgDB.init(this.absolutePath + "/MSG_ALL.db");
            final String wxid = this.tableVw.getSelectionModel().getSelectedItem().getWechatId();
            showChatWindow(wxid);
        }
    }

    public void showChatWindow(String wxid){
        try {
            final FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ppsoft1991/chatViewTool/ui/chat-window.fxml"));
            final Parent root = loader.load();
            final Scene scene = new Scene(root);
            final Stage stage = new Stage();
            stage.setScene(scene);
            // stage.setResizable(false);
            final String count = String.valueOf(MsgDB.getMsgCount(wxid));
            stage.setTitle(wxid + "("+ CommonUtils.getWxName(wxid) +")"+"-消息数:" + count);
            final ChatController chatWindowCtr = loader.getController();
            chatWindowCtr.setInitValue(wxid, this.loadHd.isSelected(), Integer.parseInt(count), this.absolutePath);
            stage.showAndWait();

            stage.setOnCloseRequest((WindowEvent)-> System.gc());
        }
        catch (Exception err) {
            if (err.getMessage() == null) {
                System.out.println(":)");
            }
        }
    }

    public void onMsgInfoButtonClick() throws Exception {
        final File dir = openFolderChooser("选择已解密的数据库文件目录");
        if (dir != null) {
            this.getDbFileList(dir.getAbsolutePath(), false);
            this.absolutePath = dir.getAbsolutePath();
            MicroMsgDB.init(this.absolutePath+"/MicroMsg.db_dec.db");
            // 清空缓存
            CommonUtils.WXID_MAP = new HashMap<>();
            CommonUtils.WXHEADER_MAP = new HashMap<>();
            this.getSessionList();
        }
    }

    public void onMsgInfoLoad(File dir) throws Exception {
        if (dir != null) {
            this.getDbFileList(dir.getAbsolutePath(), false);
            this.absolutePath = dir.getAbsolutePath();
            MicroMsgDB.init(this.absolutePath+"/MicroMsg.db_dec.db");
            this.getSessionList();
        }
    }

    private void getSessionList() throws Exception {
        this.initTableView();
        MicroMsgDB.init(this.absolutePath+"/MicroMsg.db_dec.db");
        // MicroMsgDB.getAllUserHeadUrl(absolutePath);
        CommonUtils.SELF_WXID = MicroMsgDB.getSelfUserName();
        List<SessionMapper> sessionMappers = MicroMsgDB.queryStrUsrName();
        // 获取所有用户的wechatid，来判断有没有有头像
        HashMap<String, String> wechatHeadImage = new HashMap<>();
        List<String> waitDownImage = new ArrayList<>();
        if (!sessionMappers.isEmpty()){
            for (SessionMapper mapper: sessionMappers){
                String wxid = mapper.getStrUsrName();
                final String imgPath = absolutePath + "/.imgcache/" + wxid + ".jpg";
                if (Files.exists(Paths.get(imgPath))) {
                    String headerImgPath = "file:"+imgPath;
                    wechatHeadImage.put(wxid, headerImgPath);
                    CommonUtils.WXHEADER_MAP.put(wxid, headerImgPath);
                }else {
                    wechatHeadImage.put(wxid, CommonUtils.BLACK_Header);
                    waitDownImage.add(wxid);
                }
            }
        }
        CommonUtils.SELF_WX_HEAD = CommonUtils.WXHEADER_MAP.get(CommonUtils.SELF_WXID);
        List<SessionMapper> allSessionMappers = MicroMsgDB.queryALLSession();
        Files.createDirectories(Paths.get(this.absolutePath + "/.imgcache"));
        if (!allSessionMappers.isEmpty()) {
            this.loadHd.setVisible(true);
            this.tableVw.getItems().clear();
            for (SessionMapper mapper: allSessionMappers){
                final String strUsrName = mapper.getStrUsrName();
                final String strContent = mapper.getStrContent();
                final Integer Reserved2 = mapper.getReserved2();
                if (!strUsrName.startsWith("gh_") && strContent.length() > 0 && !strUsrName.startsWith("@publicUser") && Reserved2 != 5) {
                    final ContactMapper userInfo = this.getUserInfo(strUsrName);
                    if (userInfo == null) {
                        continue;
                    }
                    String wxName = "";
                    if (userInfo.getUserName()!=null){
                        if (!Objects.equals(userInfo.getAlias(), "")) {
                            wxName = userInfo.getNickName() + "(" + userInfo.getAlias() + ")";
                        }else {
                            wxName = userInfo.getNickName();
                        }
                    }
                    CommonUtils.WXID_MAP.put(userInfo.getUserName(), wxName);
                    this.pushToView(wechatHeadImage.get(strUsrName), userInfo.getUserName(), userInfo.getAlias(), userInfo.getNickName(), userInfo.getRemark(), strContent, "");
                }
            }
            // 遍历所有ID，下载头像
            (new Thread(()->{
                for (String wxid: waitDownImage){
                    try {
                        HeaderIconMapper userHeadUrl = MicroMsgDB.getUserHeadUrl(wxid);
                        if (userHeadUrl.getSmallHeadImgUrl() != null) {
                            // 如果有高清图就下高清 【-】 高清很多无法查看，撤回
//                                if (headUrl[1]!=null && headUrl[1].startsWith("http")){
//                                    headUrl[0] = headUrl[1];
//                                }
                            final String headImagePath = absolutePath + "/.imgcache/" + wxid + ".jpg";
                            ChatController.downloadImg(userHeadUrl.getSmallHeadImgUrl(), headImagePath);
                            List<TableList> lst = this.tableVw.getItems();
                            for (int i = 0; i < lst.size(); i++) {
                                if (wxid.equals(lst.get(i).getWechatId())) {
                                    lst.get(i).setImage("file:" + absolutePath + "/.imgcache/" + wxid + ".jpg");
                                    this.tableVw.getItems().set(i, lst.get(i));
                                    break;
                                }
                            }
                        }
                    }catch (Exception e){
                        System.out.println("[-] 下载头像错误，网络问题");
                    }

                }
                image.setCellFactory(param -> new TableCell<Object, Object>(){
                    @Override
                    public void updateItem(Object item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item!=null&&!empty){
                            String image = item.toString();
                            ImageView view = ChatController.setImage(image);
                            this.setGraphic(view);
                            this.setAlignment(Pos.CENTER);
                            this.setOnMouseClicked(event -> {
                                showImage(image);
                            });
                        }
                    };
                });
            })).start();
        }
        else {
            this.labelMsg.setText("查询数据库失败!");
        }
        this.labelMsg.setText("加载完成!");
    }

    public void showImage(String image){
        AnchorPane root = new AnchorPane();
        ScrollPane scrollPane = new ScrollPane();
        root.getChildren().add(scrollPane);
        VBox vBox = new VBox();
        vBox.setPrefHeight(480);
        vBox.setPrefWidth(480);
        vBox.setAlignment(Pos.CENTER);
        scrollPane.setContent(vBox);

        ImageView imgHead = new ImageView(image);
        imgHead.setFitHeight(480);
        imgHead.setFitWidth(480);
        vBox.getChildren().add(imgHead);

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void onSearchButtonClicked() throws Exception {
        String user;
        String chatText;
        int count = 0;
        HashSet<String> userList = new HashSet<>();
        String text = this.filterText.getText();
        MsgDB.init(this.absolutePath + "/MSG_ALL.db");
        List<MsgMapper> msgMappers = MsgDB.searchMessageFromText(text);
        if (msgMappers.size()>0){
            try {
                for(MsgMapper msgMapper: msgMappers){
                    user = msgMapper.getStrTalker();
                    chatText = msgMapper.getStrContent();
                    System.out.println("[+]\t搜索到用户\t"+user+" -> "+chatText);
                    count = count+1;
                    userList.add(user);
                }
            }catch (NullPointerException exception1){
                System.out.println("[!] 未加载数据库!");
            }catch (Exception exception2){
                System.out.println("[!] 数据库未索引，开始自动索引，重新点击搜索");
            }
            System.out.println("[!] 搜索结束，共涉及"+userList.size()+"个用户");
            System.out.println("-> "+userList);
        }

    }


    public void onButtonClickDec() throws Exception {
        final File dir = openFolderChooser("选择包含有秘钥以及数据库的文件夹");
        if (dir != null) {
            final String Path = dir.getAbsolutePath();
            final String keyPath = Path + "/DBPass.Bin";
            if (this.fileIsExists(Path + "/DBPass.Bin")) {
                if (this.fileIsExists(Path + "/MSG0.db")) {
                    final List<String> dbList = this.getDbFileList(Path, true);
                    for (final String dbName : dbList) {
                        final String dbPath = Path + "/" + dbName;
                        this.decryptDb(keyPath, dbPath);
                    }
                    this.labelMsg.setText("完成，共解密数据库" + dbList.size() + "个");
                    CommonFunc.mergeDb(dbList, Path);
                    // 解密完，直接打开
                    onMsgInfoLoad(dir);
                }
                else {
                    this.labelMsg.setText("数据库文件缺失!");
                }
            }
            else {
                this.labelMsg.setText("秘钥文件不存在！");
            }
        }
    }



    public void decryptDb(final String inKeyPath, final String inDbPath){
        try {
            if (!inDbPath.contains("MSG_ALL")) {
                (new WechatDb(inKeyPath, inDbPath)).decryptDb();
            }
        }catch (java.security.InvalidKeyException ignored){
            System.out.println("[-] 出错，参考JCE无限制权限策略文件 参考https://blog.csdn.net/weixin_44857862/article/details/124589684");
        }catch (NumberFormatException e){
            System.out.println("[-] 出错，检查DBPass.Bin文件编码是否为UTF-8");
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    private ContactMapper getUserInfo(final String wxid){
        ContactMapper contactMapper = null;
        try {
            contactMapper = MicroMsgDB.queryUserContact(wxid);
        }
        catch (Exception e) {
            System.out.println("[-]" + e.getMessage() + ": " + " Get user Info Error");
        }
        return contactMapper;
    }

    public static File openFolderChooser(final String wTitle) {
        final DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(wTitle);
        return directoryChooser.showDialog(new Stage());
    }


    public List<String> getDbFileList(final String path, final boolean encrypted) {
        final List<String> deFileList = new ArrayList<>();
        final List<String> enFileList = new ArrayList<>();
        final File dir = new File(path);
        final int len = Objects.requireNonNull(dir.list()).length;
        final String[] files = dir.list();
        for (int i = 0; i < len; ++i) {
            assert files != null;
            if (files[i].endsWith("_dec.db")) {
                deFileList.add(files[i]);
            }
            else if (files[i].endsWith(".db")) {
                enFileList.add(files[i]);
            }
        }
        if (encrypted) {
            return enFileList;
        }
        return deFileList;
    }

    public boolean fileIsExists(final String Path) {
        return new File(Path).exists();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadHd.setSelected(true);
    }

    public void onUserChat(ActionEvent actionEvent) throws Exception {
        final String text = filterText.getText();
        if (!text.isEmpty()){
            MsgDB.init(this.absolutePath + "/MSG_ALL.db");
            showChatWindow(text);
        }else {
            labelMsg.setText("[-] 搜索框输入的是空的呢!");
        }
    }
}
