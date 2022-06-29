module org.mtuciflix.mtuciflixadminui {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires mtuciflix.api;

    opens org.mtuciflix.mtuciflixadminui to javafx.fxml;
    opens org.mtuciflix.mtuciflixadminui.forms to javafx.fxml;
    exports org.mtuciflix.mtuciflixadminui;
    exports org.mtuciflix.mtuciflixadminui.forms;
}