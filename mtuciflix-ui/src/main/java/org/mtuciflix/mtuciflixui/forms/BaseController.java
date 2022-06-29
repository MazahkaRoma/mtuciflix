package org.mtuciflix.mtuciflixui.forms;

import javafx.scene.input.MouseEvent;

public interface BaseController {

    void init(Object object);

    void close(MouseEvent mouseEvent);

    void hide(MouseEvent mouseEvent);

    void fullscreen(MouseEvent mouseEvent);
}
