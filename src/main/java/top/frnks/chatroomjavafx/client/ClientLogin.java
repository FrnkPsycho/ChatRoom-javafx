package top.frnks.chatroomjavafx.client;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import top.frnks.chatroomjavafx.client.util.ClientUtil;
import top.frnks.chatroomjavafx.common.model.entity.ActionType;
import top.frnks.chatroomjavafx.common.model.entity.Request;
import top.frnks.chatroomjavafx.common.model.entity.Response;
import top.frnks.chatroomjavafx.common.model.entity.User;
import top.frnks.chatroomjavafx.common.util.TranslatableString;

import java.io.IOException;

public class ClientLogin {
    public static final VBox loginRoot = new VBox();
    public static final TextField idField = new TextField();
    public static final PasswordField passwordField = new PasswordField();
    public static final Button loginButton = new Button(new TranslatableString("client.login.button.login").translate());
    public static final Button signupButton = new Button(new TranslatableString("client.login.button.signup").translate());
    public static final Stage stage = new Stage();


    public static void showWindow() {
        // debug
//        ClientDataBuffer.currentUser = new User(88888, "DEBUG_CURRENT_USER", "password");


        HBox idBox = new HBox();
        idBox.getChildren().add(new Label(new TranslatableString("client.login.label.id").translate()));
        idBox.getChildren().add(idField);

        HBox pwBox = new HBox();
        pwBox.getChildren().add(new Label(new TranslatableString("client.login.label.password").translate()));
        pwBox.getChildren().add(passwordField);

        HBox buttonBox = new HBox();
        buttonBox.getChildren().add(loginButton);
        buttonBox.getChildren().add(signupButton);

        loginButton.setOnAction(event -> login());
        signupButton.setOnAction(event -> signup());

        loginRoot.getChildren().add(idBox);
        loginRoot.getChildren().add(pwBox);
        loginRoot.getChildren().add(buttonBox);

        Scene scene = new Scene(loginRoot);
        stage.setTitle(new TranslatableString("client.window.login_title").translate());
        stage.setScene(scene);
        stage.showAndWait();
    }
    private static void login() {
        Request request = new Request();
        request.setAction(ActionType.LOGIN);
        request.setAttribute("id", idField.getText());
        request.setAttribute("password", ClientUtil.digestPassword(passwordField.getText()));

        Response response = null;
        try {
            response = ClientUtil.sendRequestWithResponse(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // TODO: debug
        ClientDataBuffer.currentUser = new User(88888, "YOU", "1234");
        ClientDataBuffer.isLoggedIn = true;
        stage.close();
    }

    private static void signup() {

    }
}
