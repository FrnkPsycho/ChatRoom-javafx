module top.frnks.chatroomjavafx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;

    exports top.frnks.chatroomjavafx.client;
    opens top.frnks.chatroomjavafx.client to javafx.fxml;
}