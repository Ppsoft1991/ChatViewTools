package com.ppsoft1991.chatViewTool.fx;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.text.Font;


public class MessageSelfLabel extends Label {
    private static final Font messageFont = new Font(14.0);
    private static final Insets  insets = new Insets(6.0);

    public MessageSelfLabel(){
        super();
        this.setWrapText(true);
        this.setMaxWidth(220.0);
        this.setStyle("-fx-background-color: rgb(84,225,81); -fx-background-radius: 8px;");
        this.setPadding(insets);
        this.setFont(messageFont);
    }
}
