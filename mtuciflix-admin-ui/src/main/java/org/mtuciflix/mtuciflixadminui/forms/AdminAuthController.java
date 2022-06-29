package org.mtuciflix.mtuciflixadminui.forms;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import mtuciflix.api.stubs.AdminClient;

import java.io.File;

public class AdminAuthController {
    @FXML
    public Button searchImgPreview;
    @FXML
    public Button searchMedia;
    @FXML
    public TextArea descriptionInput;
    @FXML
    private CheckBox isVisibleCheck;
    @FXML
    private Button submitBtn;
    @FXML
    private ImageView previewBox;
    @FXML
    private TextField mediaInput;
    @FXML
    private TextField imgPreviewInput;
    @FXML
    private TextField mediaIdInput;
    @FXML
    private TextField titleInput;
    @FXML
    private TextField directorInput;
    @FXML
    private TextField Log;
    private AdminClient client = new AdminClient("51.250.108.85", 555);

    public void submit(MouseEvent mouseEvent) {
        this.submitBtn.setDisable(true);

        logRes("Connecting to server");
        Log.setVisible(true);
        try {
            Thread send = new Thread(() -> {
                logRes("Scanning for media");
                File mediaFile = new File(mediaInput.getText());
                if (!mediaFile.exists()) {
                    Log.setText("[ERROR]:No tmp file found");
                    return;
                }
                logRes("Scanning for preview");
                File imgFile = new File(imgPreviewInput.getText());

                if (!mediaFile.exists()) {
                    Log.setText("[ERROR]:No preview image found");
                    return;
                }
                logRes("Uploading media");
                client.upload(mediaFile.getAbsolutePath(), "mp4", mediaIdInput.getText());
                logRes("Uploading preview");
                client.upload(imgFile.getAbsolutePath(), "jpg", mediaIdInput.getText());

                Log.setText("Insert to DB");
                boolean res = client.insertMedia(
                        Integer.parseInt(mediaIdInput.getText()),
                        Integer.parseInt(mediaIdInput.getText()),
                        1,
                        titleInput.getText(),
                        directorInput.getText(),
                        descriptionInput.getText(),
                        isVisibleCheck.isSelected()
                );
                if (!res) {
                    Log.setText("[ERROR]: during sending file to server.");
                } else {
                    Log.setText("Complete!!!");
                }
            });
            send.start();
            this.submitBtn.setDisable(false);
        } catch (Exception e) {
            Log.setText("[ERROR]: during sending file to server;");
        }
    }

    public void searchImgFile(MouseEvent mouseEvent) {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpeg", "*.jpg", "*.bmp"));
        File file = chooser.showOpenDialog(null);
        if (file == null) return;
        imgPreviewInput.setText(file.getPath());
        previewBox.setSmooth(true);
        previewBox.setImage(new Image(file.toURI().toString()));

    }

    public void searchMediaFile(MouseEvent mouseEvent) {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Media files", "*.mkv", "*.avi", "*.mp4"));
        File file = chooser.showOpenDialog(null);
        if (file == null) return;
        mediaInput.setText(file.getPath());
    }

    private void logRes(String msg) {
        Log.setText(msg);
    }
}
