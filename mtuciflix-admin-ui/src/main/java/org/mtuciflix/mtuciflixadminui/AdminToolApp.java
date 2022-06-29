package org.mtuciflix.mtuciflixadminui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminToolApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(AdminToolApp.class.getResource("forms/admin-auth.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("MTUCIFLIX: Admin Tool");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}