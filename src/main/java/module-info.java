module top.frnks.chatroomjavafx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires java.logging;

    exports top.frnks.chatroomjavafx.client;
    exports top.frnks.chatroomjavafx.common.type;
//    exports top.frnks.chatroomjavafx.common.util;
    exports top.frnks.chatroomjavafx.common.model.entity;
    opens top.frnks.chatroomjavafx.client to javafx.fxml;
}