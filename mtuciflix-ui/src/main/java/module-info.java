module org.mtuciflix.mtuciflixui {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires mtuciflix.api;
    requires javafx.media;
    requires uk.co.caprica.vlcj;
    requires uk.co.caprica.vlcj.javafx;

    opens org.mtuciflix.mtuciflixui to javafx.fxml;
    opens org.mtuciflix.mtuciflixui.forms to javafx.fxml;
    exports org.mtuciflix.mtuciflixui;
    exports org.mtuciflix.mtuciflixui.forms;
}