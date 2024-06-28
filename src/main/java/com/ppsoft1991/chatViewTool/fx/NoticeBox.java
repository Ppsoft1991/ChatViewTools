package com.ppsoft1991.chatViewTool.fx;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

/**
 * @author roto
 */
public class NoticeBox extends HBox {
    private static final Insets insets = new Insets(10.0, 5.0, 10.0, 5.0);
    private static final Insets  labelInsets = new Insets(6.0);

    public NoticeBox(String message) {
        super();
        this.setPadding(insets);
        this.setPrefWidth(500.0);
//        TextArea tf = new TextArea(message);
//        tf.setEditable(false);
//        tf.setWrapText(true);
//        tf.setMaxWidth(400);
//        this.setStyle("-fx-background-color: #c0c0c0;-fx-opacity: 0;");
//        tf.setPadding(new Insets(10.0, 5.0, 10.0, 5.0));
//        tf.setFont(new Font(12.0));
        Label label = new Label();
        label.setWrapText(true);
        label.setText(message);
        label.setMaxWidth(400);
        label.setPadding(labelInsets);
        label.setStyle("-fx-background-color: #c0c0c0;-fx-background-radius: 8px;");
        label.setFont(new Font(12.0));
        this.getChildren().add(label);
        this.setAlignment(Pos.TOP_CENTER);
        this.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount()==2){
                    System.out.println(label.getText());
                }
            }
        });
    }
}
