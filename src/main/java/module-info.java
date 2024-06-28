module com.ppsoft1991.chatViewTool {
    requires javafx.fxml;
    requires org.bouncycastle.provider;
    requires java.desktop;
    requires java.sql;
    requires javafx.media;
    requires com.github.albfernandez.juniversalchardet;
    requires spring.jdbc;
    requires c3p0;
    requires javafx.controls;
    requires com.google.protobuf;


    opens com.ppsoft1991.chatViewTool to javafx.fxml;
    opens com.ppsoft1991.chatViewTool.fx to javafx.fxml, java.base;
    exports com.ppsoft1991.chatViewTool;
    exports com.ppsoft1991.chatViewTool.utils;
    exports com.ppsoft1991.chatViewTool.mapper.microMsg;
    exports com.ppsoft1991.chatViewTool.mapper.msg;
    opens com.ppsoft1991.chatViewTool.utils to javafx.fxml;
    opens com.ppsoft1991.chatViewTool.controller to javafx.fxml;
    exports com.ppsoft1991.chatViewTool.fx;
}