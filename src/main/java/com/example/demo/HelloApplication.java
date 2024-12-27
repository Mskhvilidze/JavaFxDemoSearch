package com.example.demo;

import com.example.demo.manager.ScannerManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        stage.setMinHeight(490.0);
        stage.setMinWidth(490.0);
        new ScannerManager(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}