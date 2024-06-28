package com.ppsoft1991.chatViewTool.fx;

import com.ppsoft1991.chatViewTool.controller.ChatController;
import com.ppsoft1991.chatViewTool.utils.CommonUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;

import java.io.File;

public class BubbleBox extends HBox {
    private static final Insets insets = new Insets(10.0, 5.0, 10.0, 5.0);

    public BubbleBox(String message,String meta,boolean selfSend, ImageView imgHead){
        super();
        setPadding(insets);
        setSpacing(10.0);
        ImageView head = null;
        Bubble bubbleBox = new Bubble(message, meta);
        HBox.setHgrow(bubbleBox, Priority.ALWAYS);
        if (selfSend){
            bubbleBox.setBubbleColor(Color.rgb(84, 225, 81));
            head = CommonUtils.selfImage();
            this.setAlignment(Pos.TOP_RIGHT);
            this.getChildren().addAll(bubbleBox, head);
        }else {
            head = imgHead;
            this.getChildren().addAll(head, bubbleBox);
        }
        // 新增点击播放
        this.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
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
        });
    }
}
