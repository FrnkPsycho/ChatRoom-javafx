package top.frnks.chatroomjavafx.client;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import top.frnks.chatroomjavafx.client.util.ClientUtil;
import top.frnks.chatroomjavafx.common.model.entity.*;
import top.frnks.chatroomjavafx.common.util.TranslatableString;

import java.io.IOException;

public class ClientLogin {
    public static final VBox loginRoot = new VBox();
    public static final TextField idField = new TextField();
    public static final TextField nicknameField = new TextField();
    public static final PasswordField passwordField = new PasswordField();
    public static final Button loginButton = new Button(new TranslatableString("client.login.button.login").translate());
    public static final Button signupButton = new Button(new TranslatableString("client.login.button.signup").translate());
    public static final Stage stage = new Stage();


    public static void showWindow() {
        HBox idBox = new HBox();
        idBox.getChildren().add(new Label(new TranslatableString("client.login.label.id").translate()));
        idBox.getChildren().add(idField);

        HBox pwBox = new HBox();
        pwBox.getChildren().add(new Label(new TranslatableString("client.login.label.password").translate()));
        pwBox.getChildren().add(passwordField);

        HBox nameBox = new HBox();
        nameBox.getChildren().add(new Label(new TranslatableString("client.login.label.nickname").translate()));
        nameBox.getChildren().add(nicknameField);

        HBox buttonBox = new HBox();
        buttonBox.getChildren().add(loginButton);
        buttonBox.getChildren().add(signupButton);

        loginButton.setOnAction(event -> login());
        signupButton.setOnAction(event -> signup());

        loginRoot.getChildren().add(idBox);
        loginRoot.getChildren().add(nameBox);
        loginRoot.getChildren().add(pwBox);
        loginRoot.getChildren().add(buttonBox);
        loginRoot.getChildren().add(new Label(new TranslatableString("client.login.hint").translate()));

        Scene scene = new Scene(loginRoot);
        stage.setTitle(new TranslatableString("client.window.login_title").translate());
        stage.setScene(scene);
        stage.showAndWait();
    }
    public static void login() {
        if ( ClientDataBuffer.clientSocket == null ) {
            Alert alert = new Alert(Alert.AlertType.ERROR, new TranslatableString("client.login.no_connection").translate());
            alert.show();
            return;
        }
        if ( (idField.getText().isBlank() && nicknameField.getText().isBlank()) || passwordField.getText().isBlank() ) {
            Alert alert = new Alert(Alert.AlertType.ERROR, new TranslatableString("client.login.login_missing_fields").translate());
            alert.show();
            return;
        }

        Request request = new Request();
        request.setAction(ActionType.LOGIN);
        request.setAttribute("id", idField.getText());
        request.setAttribute("nickname", nicknameField.getText());
        request.setAttribute("password", passwordField.getText());

        try {
            ClientUtil.sendRequestWithoutResponse(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void signup() {
        if ( ClientDataBuffer.clientSocket == null ) {
            ClientUtil.popAlert(Alert.AlertType.ERROR, "client.login.no_connection");
            return;
        }

        // fields check
        if ( nicknameField.getText().isBlank() || passwordField.getText().isBlank() ) {
            ClientUtil.popAlert(Alert.AlertType.ERROR, "client.login.signup_missing_fields");
            return;
        }

        Request request = new Request();
        request.setAction(ActionType.SIGNUP);
        request.setAttribute("nickname", nicknameField.getText());
        request.setAttribute("password", passwordField.getText());

        try {
            ClientUtil.sendRequestWithoutResponse(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // signup response handler moves to ClientAction due to some stream issue
    }
}
