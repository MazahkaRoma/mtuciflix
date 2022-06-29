package org.mtuciflix.mtuciflixui.forms;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Utils {

    public static Stage getStageFromEvent(Event event) {
        Node node = (Node) event.getSource();
        return (Stage) node.getScene().getWindow();
    }

    public static void updateStageWithInitObject(Event event,
                                                 Object objectForInit,
                                                 String formName) throws IOException {
        URL pathToForm = Utils.class.getResource(formName);
        FXMLLoader loader = new FXMLLoader(pathToForm);
        Stage oldStage = Utils.getStageFromEvent(event);
        oldStage.setScene(new Scene(loader.load()));
        BaseController controller = loader.getController();
        controller.init(objectForInit);
    }

    public static void close(Event event) {
        Stage stage = getStageFromEvent(event);
        stage.close();
    }

    public static void hide(Event event) {
        Stage stage = getStageFromEvent(event);
        stage.setIconified(true);
    }

    public static void fullscreen(Event event) {
        Stage stage = getStageFromEvent(event);
        stage.setFullScreen(!stage.isFullScreen());

    }
}
