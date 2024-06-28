package com.ppsoft1991.chatViewTool;

import com.ppsoft1991.chatViewTool.utils.CommonUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ChatAppApplication extends Application {
    @Override
    public void start(final Stage stage) throws IOException {
        stage.setResizable(false);
        final FXMLLoader fxmlLoader = new FXMLLoader(CommonUtils.getResource("ui/chat-application.fxml"));
        stage.getIcons().add(new Image(Objects.requireNonNull(CommonUtils.resource("ui/logo.png"))));

        final Scene scene = new Scene(fxmlLoader.load(), 970.0, 450.0);
        stage.setTitle("微信数据库解密查看器 " + GlobalConfig.VERSION);
        final String css = CommonUtils.getResource("ui/main.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(final String[] args) {
        Application.launch();
    }
}
