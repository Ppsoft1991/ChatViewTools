package com.ppsoft1991.chatViewTool.fx;

import com.ppsoft1991.chatViewTool.controller.ChatController;
import com.ppsoft1991.chatViewTool.utils.CommonUtils;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.io.File;

public class MessageBox extends HBox {

    private static final Insets insets = new Insets(10.0, 5.0, 10.0, 5.0);
    private static final Insets friendInsets = new Insets(15.0, 0.0, 0.0, 10.0);
    private static final Insets selfInsets = new Insets(15.0, 10.0, 0.0, 0.0);

    public MessageBox(String message,boolean selfSend, ImageView imgHead){
        super();
        setPadding(insets);
        if (selfSend){
            Polygon selfPolygon = new Polygon(0.0, 0.0, 0.0, 10.0, 10.0, 5.0);
            selfPolygon.setFill(Color.rgb(84, 225, 81));
            HBox.setMargin(selfPolygon, selfInsets);
            MessageSelfLabel messageSelfLabel = new MessageSelfLabel();
            messageSelfLabel.setText(message);
            HBox.setHgrow(messageSelfLabel, Priority.ALWAYS);
            this.getChildren().addAll(messageSelfLabel, selfPolygon, CommonUtils.selfImage());
            this.setAlignment(Pos.CENTER_RIGHT);
        }else {
            Polygon friendPolygon = new Polygon(0.0, 5.0, 10.0, 0.0, 10.0, 10.0);
            friendPolygon.setFill(Color.rgb(179,231,244));
            HBox.setMargin(friendPolygon, friendInsets);
            MessageFriendsLabel messageFriendsLabel = new MessageFriendsLabel();
            HBox.setHgrow(messageFriendsLabel, Priority.ALWAYS);
            messageFriendsLabel.setText(message);
            this.getChildren().addAll(imgHead, friendPolygon, messageFriendsLabel);
        }
        // 新增点击播放
        this.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount()==2){
                    for (Node n:getChildren()){
                        if (n instanceof Label){
                            String[] s = ((Label)n).getText().split("\n");
                            if (s.length==2 && s[1].startsWith("[wave]")){
                                String msgSvrId = s[1].substring(6);
                                try {
                                    String chatMedia = CommonUtils.getChatMedia(ChatController.absolutePath, msgSvrId);
                                    if (chatMedia!=null){
                                        (new Thread(()->{
                                            (new MediaPlayer(new Media((new File(chatMedia).toURI().toString())))).play();
                                        })).start();
                                    }else {
                                        System.out.println("[-] 数据库中没有此条语音");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    System.out.println("[-] 播放语音失败");
                                }
                            }else {
                                // 双击输出消息
                                System.out.println(s[1]);
                            }
                        }
                    }
                }
            }
        });
    }
}
