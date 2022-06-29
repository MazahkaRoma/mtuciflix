package org.mtuciflix.mtuciflixui.forms;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import mtuciflix.api.stubs.MediaClient;

import java.io.IOException;
import java.util.Map;

public class MainScreenController implements BaseController {

    @FXML
    public ImageView testImageView;
    @FXML
    public ScrollPane scrollBar;
    @FXML
    private Button libraryButton;
    @FXML
    private Button favoriteButton;
    @FXML
    private TilePane mediaList;
    private MediaClient mediaClient;
    private final String mediaContent = "http://51.250.108.85/media/";
    private String imageDef = ".jpg";

    @FXML
    public void showUserLibrary() {
        final double SPEED = 1.5f;
        scrollBar.getContent().setOnScroll(scrollEvent -> {
            double deltaY = scrollEvent.getDeltaY() * SPEED;
            scrollBar.setVvalue(scrollBar.getVvalue() - deltaY);
        });
        mediaList.getChildren().clear();

        Map<Integer, Long> mediaMap = mediaClient.getListOfMedia();

        for (int mediaId : mediaMap.keySet()) {
            try {
                ImageView mediaIcon = new ImageView();
                mediaIcon.setFitHeight(200);
                mediaIcon.setFitWidth(160);
                setMediaIconDefaultProperties(mediaIcon, mediaId);
                setMediaIconDefaultHandlers(mediaIcon, mediaId);


                mediaList.getChildren().add(mediaIcon);
                TilePane.setMargin(mediaIcon, new Insets(12));
            } catch (Exception ignore) {
                System.out.println(ignore.getMessage());
            }
        }
    }

    @FXML
    public void showUserFavorite() {
    }

    @FXML
    public void showUserProfile() {
    }

    @FXML
    public void OnLoad() {

    }

    private void setMediaIconDefaultProperties(ImageView mediaIcon, int mediaId) {
        String mediaUrl = mediaContent + mediaId + imageDef;
        mediaIcon.setPreserveRatio(false);
        mediaIcon.setCursor(Cursor.HAND);
        mediaIcon.setImage(new Image(mediaUrl));
        mediaIcon.setStyle("-fx-background-color: white;" +
                "-fx-effect: dropshadow(gaussian, gray, 10, 0, 0, 0);");
    }

    private void setMediaIconDefaultHandlers(ImageView mediaIcon, int mediaId) {
        mediaIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
                    try {
                        Utils.updateStageWithInitObject(mouseEvent, mediaClient.getMediaInfo(mediaId), FormsRegistry.MEDIA_INFO_FORM);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );
        mediaIcon.addEventHandler(MouseEvent.MOUSE_EXITED, mouseEvent ->
                mediaIcon.setStyle("-fx-background-color: white;" +
                        "-fx-effect: dropshadow(gaussian, gray, 10, 0, 0, 0);"));
        mediaIcon.addEventHandler(MouseEvent.MOUSE_ENTERED, mouseEvent ->
                mediaIcon.setStyle("-fx-background-color: white;" +
                        "-fx-effect: dropshadow(gaussian, white, 10, 0, 0, 0);"));
    }

    @Override
    public void init(Object object) {
        this.mediaClient = (MediaClient) object;
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

}
