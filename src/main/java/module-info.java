module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires java.desktop;
    requires com.google.common;
    requires org.fxmisc.richtext;
    requires reactfx;

    opens com.example.demo to javafx.fxml;
    opens com.example.demo.model to javafx.base;
    opens com.example.demo.manager to com.google.common;
    opens com.example.demo.message to com.google.common;
    exports com.example.demo;
}