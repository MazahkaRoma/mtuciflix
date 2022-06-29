package org.mtuciflix.mtuciflixui.forms;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class MtuciflixRunner extends Application {
    private double xOffset = 0;
    private double yOffset = 0;

    public MtuciflixRunner() throws IOException {
    }

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource(FormsRegistry.AUTH_SCENE));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        BaseController controller = loader.getController();
        controller.init(null);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);

        stage.show();
    }

    public static void main(String[] args){
        Application.launch(args);
    }
}
