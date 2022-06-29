package org.mtuciflix.mtuciflixui.forms;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.*;
import mtuciflix.api.utils.MediaInfo;

import java.io.IOException;

public class MediaInfoController implements BaseController {
    @FXML
    public Text descriptionText;
    @FXML
    public Text titleText;
    @FXML
    public Text directorText;
    @FXML
    public Button playBtn;
    @FXML
    public ImageView mediaIcon;
    @FXML
    public Button backBtn;
    @FXML
    public Button addToFavoriteBtn;

    MediaInfo mediaInfo;

    public void playMedia(MouseEvent mouseEvent) {

    }

    @Override
    public void init(Object info) {
        this.mediaInfo = (MediaInfo) info;

        titleText.setText(mediaInfo.getTitle());
        descriptionText.setText(mediaInfo.getDescription());
        directorText.setText(mediaInfo.getDirector());

        mediaIcon.setImage(new Image("http://51.250.108.85/media/" + mediaInfo.getMediaId() + ".jpg"));

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

    @FXML
    public void runMediaPlayer(MouseEvent mouseEvent) {
        try {
            Utils.updateStageWithInitObject(mouseEvent, mediaInfo, FormsRegistry.MEDIA_PLAYER_SCENE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void backToList(MouseEvent mouseEvent) {
        try {
            Utils.updateStageWithInitObject(mouseEvent, null, FormsRegistry.MAIN_FORM);
        } catch (Exception ignore) {
        }
    }

}
