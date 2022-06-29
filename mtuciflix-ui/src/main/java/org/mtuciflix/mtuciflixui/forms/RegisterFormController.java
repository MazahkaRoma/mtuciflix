package org.mtuciflix.mtuciflixui.forms;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import mtuciflix.api.stubs.MediaClient;

public class RegisterFormController implements BaseController {
    @FXML
    public TextField login;
    @FXML
    public TextField passwordConfirm;
    @FXML
    public TextField password;
    private MediaClient client;

    @Override
    public void init(Object object) {
        this.client = (MediaClient) object;
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
        Utils.fullscreen(mouseEvent);
    }

    public boolean checkText(String text) {
        return text.matches("[a-zA-Z1-9@$:?+-;()#^&!/*\\[\\]{}'.,№;%~`]+") && !text.matches("[ ]*");
    }

    @FXML
    public void register(MouseEvent mouseEvent) {
        String login = this.login.getText();
        String password = this.password.getText();
        String passwordConfirm = this.passwordConfirm.getText();
        if (!password.equals(passwordConfirm)) {
            System.out.println("password not match");
        } else if (checkText(login) && checkText(password) && checkText(passwordConfirm)) {
            client.register(login, password);
            try {
                Utils.updateStageWithInitObject(mouseEvent, client, FormsRegistry.MAIN_FORM);
            } catch (Throwable e){
                e.printStackTrace();
            }

        }
    }
}
