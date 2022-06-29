package org.mtuciflix.mtuciflixui.forms;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import mtuciflix.api.stubs.MediaClient;

public class AuthFormController implements BaseController {

    MediaClient client;
    @FXML
    public TextField passwordTxt;
    @FXML
    public TextField loginTxt;
    @FXML
    public Button closeBtn;
    @FXML
    public Button loginBtn;
    @FXML
    public Button registerBtn;

    @Override
    public void init(Object object) {
        this.client = new MediaClient("51.250.108.85", 555);
    }

    @Override
    public void close(MouseEvent mouseEvent) {
        Utils.close(mouseEvent);
    }

    @Override
    public void hide(MouseEvent mouseEvent) {
        Utils.hide(mouseEvent);
    }

    @Override
    public void fullscreen(MouseEvent mouseEvent) {

    }

    @FXML
    public void login(MouseEvent mouseEvent) {
        if (client.auth(loginTxt.getText(), passwordTxt.getText())) {
            try {
                Utils.updateStageWithInitObject(mouseEvent, client, FormsRegistry.MAIN_FORM);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void register(MouseEvent mouseEvent) {
        try {
            Utils.updateStageWithInitObject(mouseEvent, client, FormsRegistry.REGISTER_SCENE);
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

}
