package com.example.demo.manager;

import com.example.demo.HelloController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class ScannerManager {
    private Stage primaryStage;
    private Scene scene;

    public ScannerManager(Stage stage) throws IOException {
        this.primaryStage = stage;
        initView();
    }

    public ScannerManager() {

    }

    private void initView() throws IOException {
        showScene();
    }


    private void showScene() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(HelloController.FXML));
        Parent rootParent = loader.load();
        HelloController presenter = loader.getController();
        Platform.runLater(() -> {
            primaryStage.initStyle(StageStyle.UTILITY);
            primaryStage.setTitle("Platform");
            scene = new Scene(rootParent, 900, 750);
            primaryStage.setScene(scene);
            primaryStage.show();
        });
    }
}
