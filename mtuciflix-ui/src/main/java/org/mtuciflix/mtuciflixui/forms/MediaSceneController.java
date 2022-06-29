package org.mtuciflix.mtuciflixui.forms;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import mtuciflix.api.utils.MediaInfo;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.javafx.videosurface.ImageViewVideoSurfaceFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayer;

public class MediaSceneController implements BaseController {

    @FXML
    public ImageView mediaView;
    @FXML
    public Pane pane;
    @FXML
    public VBox vBox;
    public Stage stage;
    @FXML
    public Pane controlBox;
    @FXML
    public HBox hBox;
    @FXML
    public Slider slider;
    @FXML
    public Button backBtn;
    @FXML
    public Text currentTime;
    @FXML
    public Text endTime;

    private MediaInfo mediaInfo;

    private final MediaPlayerFactory factory;
    private final EmbeddedMediaPlayer embeddedMediaPlayer;

    public MediaSceneController() {
        this.factory = new MediaPlayerFactory();
        this.embeddedMediaPlayer = factory.mediaPlayers().newEmbeddedMediaPlayer();

        this.embeddedMediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void playing(MediaPlayer mediaPlayer) {

            }

            @Override
            public void paused(MediaPlayer mediaPlayer) {
            }

            @Override
            public void stopped(MediaPlayer mediaPlayer) {
            }

            @Override
            public void timeChanged(MediaPlayer mediaPlayer, long newTime) {
                slider.adjustValue(mediaPlayer.status().time());
                currentTime.setText(formatTime(newTime));
            }

            @Override
            public void lengthChanged(MediaPlayer mediaPlayer, long newLength) {
                slider.setMax(newLength);
                endTime.setText(formatTime(newLength));
            }

        });


    }

    @Override
    public void init(Object object) {
        mediaInfo = (MediaInfo) object;
        stage = (Stage) pane.getScene().getWindow();
        mediaView.setPreserveRatio(false);
        this.embeddedMediaPlayer.videoSurface().set(ImageViewVideoSurfaceFactory.videoSurfaceForImageView(mediaView));
        mediaView.addEventHandler(
                KeyEvent.KEY_PRESSED, keyEvent -> {
                    if (keyEvent.getCode().equals(KeyCode.ALT)) {
                        stage.setFullScreen(!stage.isFullScreen());
                    }
                }
        );

        PauseTransition idle = new PauseTransition(Duration.seconds(5));
        idle.setOnFinished(e -> {
                    pane.setCursor(Cursor.NONE);
                    controlBox.setVisible(false);
                    backBtn.setVisible(false);
                }
        );
        pane.addEventHandler(MouseEvent.MOUSE_MOVED, e -> {
                    idle.playFromStart();
                    pane.setCursor(Cursor.DEFAULT);
                    controlBox.setVisible(true);
                    backBtn.setVisible(true);
                }
        );
        embeddedMediaPlayer.media().play("http://51.250.108.85/media/" + mediaInfo.getMediaId() + ".mp4");

        mediaView.fitHeightProperty().bind(stage.heightProperty());
        mediaView.fitWidthProperty().bind(stage.widthProperty());

        slider.setOnMouseClicked(mouseEvent -> {
            embeddedMediaPlayer.controls().setTime((long) slider.getValue());
        });

        slider.addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseEvent -> {
            slider.getOnMouseClicked().handle(mouseEvent);
        });


        controlBox.managedProperty().bind(vBox.visibleProperty());
        backBtn.managedProperty().bind(backBtn.visibleProperty());

        controlBox.layoutXProperty().bind(stage.widthProperty().divide(2).subtract(controlBox.widthProperty().divide(2)));
        controlBox.layoutYProperty().bind(stage.heightProperty().subtract(controlBox.getHeight()));


    }

    @Override
    public void close(MouseEvent mouseEvent) {

    }

    @Override
    public void hide(MouseEvent mouseEvent) {
        Utils.hide(mouseEvent);
    }

    @Override
    public void fullscreen(MouseEvent mouseEvent) {

    }


    public void back(MouseEvent mouseEvent) {
        try {
            embeddedMediaPlayer.release();
            Utils.updateStageWithInitObject(mouseEvent, mediaInfo, FormsRegistry.MEDIA_INFO_FORM);
        } catch (Exception ignore) {

        }
    }

    @FXML
    public void pause(MouseEvent mouseEvent) {
        embeddedMediaPlayer.controls().pause();
    }

    @FXML
    public void play(MouseEvent mouseEvent) {
        embeddedMediaPlayer.controls().play();

    }

    public void fullScreen(MouseEvent mouseEvent) {
        stage.setFullScreen(!stage.isFullScreen());
    }

    private static String formatTime(long value) {
        value /= 1000;
        int hours = (int) value / 3600;
        int remainder = (int) value - hours * 3600;
        int minutes = remainder / 60;
        remainder = remainder - minutes * 60;
        int seconds = remainder;
        return String.format("%d:%02d:%02d", hours, minutes, seconds);
    }
}
